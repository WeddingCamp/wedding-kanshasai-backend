package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
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
    fun createSession(eventId: UlidId, name: String): Result<Session> = runCatching {
        val event = eventRepository.findById(eventId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.EVENT, eventId, it)
        }
        val session = sessionRepository.createSession(event, name).getOrElse {
            throw DatabaseException.failedToInsert(Table.SESSION, it)
        }
        val quizList = quizRepository.listByEventId(event.id).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.QUIZ, "eventId", event.id, it)
        }
        sessionQuizRepository.insertQuizList(session, quizList).getOrElse {
            throw DatabaseException.failedToInsert(Table.SESSION_QUIZ, it)
        }
        session
    }

    fun setCoverScreen(sessionId: UlidId, isVisible: Boolean): Result<Unit> = runCatching {
        val session = sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }

        sessionRepository.update(
            session.apply {
                isCoverVisible = isVisible
            },
        ).getOrElse {
            throw DatabaseException.failedToUpdate(Table.SESSION, sessionId, it)
        }
        redisEventService.publish(CoverScreenRedisEvent(isVisible), sessionId)
    }

    fun setIntroductionScreen(sessionId: UlidId, introductionType: IntroductionType): Result<Unit> = runCatching {
        val session = sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }

        if (session.state != SessionState.INTRODUCTION) {
            throw InvalidStateException("Session state is not INTRODUCTION.")
        }

        sessionRepository.update(
            session.apply {
                currentIntroduction = introductionType
            },
        ).getOrElse {
            throw DatabaseException.failedToUpdate(Table.SESSION, sessionId, it)
        }
        redisEventService.publish(IntroductionRedisEvent(introductionType), sessionId)
    }

    fun setNextQuiz(sessionId: UlidId, quizId: UlidId): Result<Unit> = runCatching {
        val session = sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }
        val quiz = quizRepository.findById(quizId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.QUIZ, quizId, it)
        }

        sessionQuizRepository.find(session, quiz).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION_QUIZ, "sessionId", session.id, "quizId", quiz.id, it)
        }
        val choiceList = choiceRepository.listByQuizId(quiz.id).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.CHOICE, "quizId", quiz.id, it)
        }
        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrElse {
            throw InvalidStateException("Current state cannot set quiz")
        }

        sessionRepository.update(
            session.apply {
                state = nextState
                currentQuizId = quiz.id
            },
        ).getOrElse {
            throw DatabaseException.failedToUpdate(Table.SESSION, sessionId, it)
        }
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
