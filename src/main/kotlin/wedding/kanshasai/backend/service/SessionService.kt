package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.repository.EventRepository
import wedding.kanshasai.backend.infra.repository.SessionRepository

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
) {
    fun createSession(eventId: UlidId, name: String): Result<Session> = runCatching {
        val event = eventRepository.findById(eventId).getOrThrow() // MyBatis or NotFoundException
        sessionRepository.createSession(event, name).getOrThrow()
    }
}
