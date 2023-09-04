package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.IntroductionType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.*
import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent
import wedding.kanshasai.backend.infra.redis.event.IntroductionRedisEvent
import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEvent
import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEventChoice
import wedding.kanshasai.backend.service.exception.FailedOperationException

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
    private val redisEventService: RedisEventService,
    private val choiceRepository: ChoiceRepository,
) {
    fun createSession(eventId: UlidId, name: String): Result<Session> = runCatching {
        try {
            // TODO: Transaction切る
            val event = eventRepository.findById(eventId).getOrThrow()
            val quizList = quizRepository.listByEvent(event).getOrThrow()
            val session = sessionRepository.createSession(event, name).getOrThrow()
            sessionQuizRepository.insertQuizList(session, quizList).getOrThrow()
            session
        } catch (e: Exception) {
            throw FailedOperationException("Create session failed.", e)
        }
    }

    fun setCoverScreen(sessionId: UlidId, isVisible: Boolean): Result<Unit> = runCatching {
        try {
            val session = sessionRepository.findById(sessionId).getOrThrow()
            sessionRepository.update(session.apply { isCoverVisible = isVisible }).getOrThrow()
            redisEventService.publish(CoverScreenRedisEvent(isVisible), sessionId)
        } catch (e: Exception) {
            throw FailedOperationException("Set cover screen failed.", e)
        }
    }

    fun setIntroductionScreen(sessionId: UlidId, introductionType: IntroductionType): Result<Unit> = runCatching {
        try {
            val session = sessionRepository.findById(sessionId).getOrThrow()

            if (session.state != SessionState.INTRODUCTION) {
                throw InvalidStateException("Session state is not INTRODUCTION.")
            }

            sessionRepository.update(session.apply { currentIntroduction = introductionType }).getOrThrow()
            redisEventService.publish(IntroductionRedisEvent(introductionType), sessionId)
        } catch (e: Exception) {
            throw FailedOperationException("Set introduction screen failed.", e)
        }
    }

    fun setNextQuiz(sessionId: UlidId, quizId: UlidId): Result<Unit> = runCatching {
        try {
            val session = sessionRepository.findById(sessionId).getOrThrow()
            val quiz = quizRepository.findById(quizId).getOrThrow()
            sessionQuizRepository.findById(session, quiz).getOrThrow()
            val choiceList = choiceRepository.listByQuiz(quiz).getOrThrow()

            val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrElse {
                throw InvalidStateException("Current state cannot set quiz")
            }

            sessionRepository.update(
                session.apply {
                    state = nextState
                    currentQuizId = quiz.id
                },
            ).getOrThrow()

            redisEventService.publish(
                NextQuizRedisEvent(
                    quiz.id.toString(),
                    quiz.body,
                    quiz.type.toGrpcType(),
                    choiceList.map {
                        NextQuizRedisEventChoice(it.id.toString(), it.body)
                    },
                ),
                sessionId,
            )
        } catch (e: Exception) {
            throw FailedOperationException("Set next quiz failed.", e)
        }
    }
}
