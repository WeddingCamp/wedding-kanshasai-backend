package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.infra.mysql.repository.EventRepository

@Service
class EventService(
    private val eventRepository: EventRepository,
) {
    fun listEventAll(): Result<List<Event>> = runCatching {
        eventRepository.listAll().getOrElse {
            throw DatabaseException.failedToRetrieve(Table.EVENT, it)
        }
    }
}
