package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.value.CoverScreenType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.EventRepository
import wedding.kanshasai.backend.infra.mysql.repository.QuizRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionQuizRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionRepository
import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
    private val redisEventService: RedisEventService,
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

    fun setCoverScreen(sessionId: UlidId, coverScreenType: CoverScreenType): Result<Unit> = runCatching {
        val session = sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }
        sessionRepository.updateCoverScreen(session, coverScreenType).getOrElse {
            throw DatabaseException.failedToUpdate(Table.SESSION, sessionId, it)
        }
        redisEventService.publish(CoverScreenRedisEvent(coverScreenType), sessionId)
    }
}
