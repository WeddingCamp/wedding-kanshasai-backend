package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.infra.mysql.repository.EventRepository

@Service
class EventService(
    private val eventRepository: EventRepository,
) {
    fun listEventAll(): List<Event> {
        return eventRepository.listAll().getOrThrowService()
    }
}
