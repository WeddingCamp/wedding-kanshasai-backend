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

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
    private val redisEventService: RedisEventService,
    private val choiceRepository: ChoiceRepository,
) {
    fun createSession(eventId: UlidId, name: String): Session {
        // TODO: Transaction切る
        val event = eventRepository.findById(eventId).getOrThrowService()
        val quizList = quizRepository.listByEvent(event).getOrThrowService()
        val session = sessionRepository.createSession(event, name).getOrThrowService()
        sessionQuizRepository.insertQuizList(session, quizList).getOrThrowService()
        return session
    }

    fun setCoverScreen(sessionId: UlidId, isVisible: Boolean) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        sessionRepository.update(session.apply { isCoverVisible = isVisible }).getOrThrowService()
        redisEventService.publish(CoverScreenRedisEvent(isVisible), sessionId)
    }

    fun setIntroductionScreen(sessionId: UlidId, introductionType: IntroductionType) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.INTRODUCTION) {
            throw InvalidStateException("Session state is not INTRODUCTION.")
        }

        sessionRepository.update(session.apply { currentIntroduction = introductionType }).getOrThrowService()
        redisEventService.publish(IntroductionRedisEvent(introductionType), sessionId)
    }

    fun setNextQuiz(sessionId: UlidId, quizId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        sessionQuizRepository.findById(session, quiz).getOrThrowService()
        val choiceList = choiceRepository.listByQuiz(quiz).getOrThrowService()

        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrThrowService()

        sessionRepository.update(
            session.apply {
                state = nextState
                currentQuizId = quiz.id
            },
        ).getOrThrowService()

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
    }
}
