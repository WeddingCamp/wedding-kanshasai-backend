package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.infra.mysql.repository.EventRepository
import wedding.kanshasai.backend.service.exception.FailedOperationException

@Service
class EventService(
    private val eventRepository: EventRepository,
) {
    fun listEventAll(): Result<List<Event>> = runCatching {
        try {
            eventRepository.listAll().getOrThrow()
        } catch (e: Exception) {
            throw FailedOperationException("Failed to list event all.", e)
        }
    }
}
