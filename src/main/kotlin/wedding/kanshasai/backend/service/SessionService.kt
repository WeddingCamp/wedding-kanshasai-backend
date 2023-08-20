package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.repository.EventRepository
import wedding.kanshasai.backend.infra.repository.QuizRepository
import wedding.kanshasai.backend.infra.repository.SessionQuizRepository
import wedding.kanshasai.backend.infra.repository.SessionRepository

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
) {
    // TODO: 例外ちゃんとする
    fun createSession(eventId: UlidId, name: String): Result<Session> = runCatching {
        val event = eventRepository.findById(eventId).getOrElse {
            throw DatabaseException.failedToRetrieve("Event", eventId, it)
        }
        val session = sessionRepository.createSession(event, name).getOrThrow()
        val quizList = quizRepository.listByEventId(event.id).getOrThrow()
        sessionQuizRepository.insertAll(session, quizList).getOrThrow()
        session
    }
}
