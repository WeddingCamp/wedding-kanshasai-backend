package wedding.kanshasai.backend.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.IntroductionType
import wedding.kanshasai.backend.domain.value.QuizResultType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.*
import wedding.kanshasai.backend.infra.redis.entity.ParticipantQuizTimeRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithCountRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithResultRedisEntity
import wedding.kanshasai.backend.infra.redis.event.*

private val logger = KotlinLogging.logger {}

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
    private val redisEventService: RedisEventService,
    private val choiceRepository: ChoiceRepository,
    private val participantRepository: ParticipantRepository,
    private val participantAnswerRepository: ParticipantAnswerRepository,
    private val objectMapper: ObjectMapper,
) {
    fun createSession(eventId: UlidId, name: String): Session {
        // TODO: Transaction切る
        val event = eventRepository.findById(eventId).getOrThrowService()
        val quizList = quizRepository.listByEvent(event).getOrThrowService()
        val session = sessionRepository.createSession(event, name).getOrThrowService()
        sessionQuizRepository.insertQuizList(session, quizList).getOrThrowService()
        return session
    }

    fun findById(sessionId: UlidId): Session {
        return sessionRepository.findById(sessionId).getOrThrowService()
    }

    fun listSessions(eventId: UlidId, includeFinished: Boolean): List<Session> {
        val event = eventRepository.findById(eventId).getOrThrowService()
        return sessionRepository.listByEvent(event, includeFinished).getOrThrowService()
    }

    fun setCoverScreen(sessionId: UlidId, isVisible: Boolean) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        sessionRepository.update(session.clone().apply { isCoverVisible = isVisible }).getOrThrowService()
        redisEventService.publish(RedisEvent.Cover(isVisible, sessionId.toString()))
    }

    fun setIntroductionScreen(sessionId: UlidId, introductionType: IntroductionType) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.INTRODUCTION) {
            throw InvalidStateException("Session state is not INTRODUCTION.")
        }

        sessionRepository.update(session.clone().apply { currentIntroduction = introductionType }).getOrThrowService()
        redisEventService.publish(RedisEvent.Introduction(introductionType, sessionId.toString()))
    }

    fun finishIntroduction(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
            },
        ).getOrThrowService()

        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun finishQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.QUIZ_PLAYING) {
            throw InvalidStateException("Session state is not QUIZ_PLAYING.")
        }
        val nextState = session.state.next(SessionState.QUIZ_RESULT).getOrThrowService()

        sessionRepository.update(session.clone().apply { state = nextState }).getOrThrowService()

        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun setNextQuiz(sessionId: UlidId, quizId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrThrowService()

        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrowService()

        if (sessionQuiz.isCompleted) {
            throw InvalidStateException("Quiz is already completed. (quizId=$quizId)")
        }

        val choiceList = choiceRepository.listByQuiz(quiz).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                currentQuizId = quiz.id
            },
        ).getOrThrowService()

        redisEventService.publish(
            RedisEvent.PreQuiz(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), it.body)
                },
                session.id.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun showQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_SHOWING).getOrThrowService()
        val (quiz, choiceList) = session.getCurrentQuiz()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
            },
        ).getOrThrowService()

        redisEventService.publish(
            RedisEvent.ShowQuiz(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), it.body)
                },
                session.id.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun startQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_PLAYING).getOrThrowService()
        val (quiz, choiceList) = session.getCurrentQuiz()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
            },
        ).getOrThrowService()

        redisEventService.publish(
            RedisEvent.StartQuiz(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), it.body)
                },
                sessionId.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun showQuizResult(sessionId: UlidId, quizResultType: QuizResultType) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val currentState = session.state
        val nextState = session.state.next(SessionState.QUIZ_RESULT).getOrThrowService()
        val (quiz, choiceList, sessionQuiz) = session.getCurrentQuiz()
        val participantAnswerList = participantAnswerRepository.listBySessionQuiz(sessionQuiz).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                currentQuizResult = quizResultType
            },
        ).getOrThrowService()

        val redisEvent = when (quizResultType) {
            QuizResultType.VOTE_LIST -> {
                RedisEvent.QuizAnswerList(
                    quiz.id.toString(),
                    quiz.body,
                    quiz.type.toGrpcType(),
                    choiceList.map {
                        QuizChoiceWithCountRedisEntity(
                            it.id.toString(),
                            it.body,
                            participantAnswerList.count { participantAnswer -> participantAnswer.answer == it.id.toString() },
                        )
                    },
                    session.id.toString(),
                )
            }

            QuizResultType.RESULT -> {
                sessionQuizRepository.update(sessionQuiz.clone().apply { isCompleted = true }).getOrThrowService()
                RedisEvent.QuizResult(
                    quiz.id.toString(),
                    quiz.body,
                    quiz.type.toGrpcType(),
                    choiceList.map {
                        QuizChoiceWithResultRedisEntity(
                            it.id.toString(),
                            it.body,
                            participantAnswerList.count { participantAnswer -> participantAnswer.answer == it.id.toString() },
                            quiz.getCorrectAnswer(objectMapper).choiceIdList.contains(it.id.toString()),
                        )
                    },
                    session.id.toString(),
                )
            }

            QuizResultType.FASTEST_RANKING -> {
                RedisEvent.QuizSpeedRanking(
                    participantAnswerList
                        .filter { quiz.getCorrectAnswer(objectMapper).choiceIdList.contains(it.answer) }
                        .sortedBy { it.time }
                        .map {
                            val participant = participantRepository.findById(it.participantId).getOrThrowService()
                            ParticipantQuizTimeRedisEntity(
                                participant.name,
                                participant.imageId.toString(),
                                it.time,
                            )
                        },
                    session.id.toString(),
                )
            }

            else -> {
                throw InvalidStateException("Invalid QuizResultType. (quizResultType=$quizResultType)")
            }
        }
        redisEventService.publish(redisEvent)
        redisEventService.publishState(currentState, nextState, session.id)
    }

    fun cancelCurrentQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        if (!listOf(SessionState.QUIZ_SHOWING, SessionState.QUIZ_PLAYING).contains(session.state)) {
            throw InvalidStateException("Session state is not QUIZ_SHOWING or QUIZ_PLAYING.")
        }
        val nextState = SessionState.QUIZ_WAITING
        val quizId = session.currentQuizId ?: throw InvalidStateException("Current quiz is not set.")
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                currentQuizId = null
            },
        ).getOrThrowService()

        participantAnswerRepository.deleteBySessionQuiz(sessionQuiz).getOrThrowService()

        redisEventService.publishState(session.state, nextState, session.id)
        // TODO: クイズキャンセルイベントを送信する
        // redisEventService.publish(CancelQuizRedisEvent(), sessionId)
    }

    fun Session.getCurrentQuiz(): Triple<Quiz, List<Choice>, SessionQuiz> {
        val quizId = this.currentQuizId
        if (quizId == null) {
            throw InvalidStateException("Current quiz is not set.")
        }
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(this, quiz).getOrThrowService()
        val choiceList = choiceRepository.listByQuiz(quiz).getOrThrowService()
        return Triple(quiz, choiceList, sessionQuiz)
    }
}
