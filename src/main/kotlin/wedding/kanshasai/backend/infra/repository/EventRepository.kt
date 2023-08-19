package wedding.kanshasai.backend.infra.repository

import org.apache.ibatis.transaction.TransactionException
import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.exception.NotFoundException
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.mapper.EventMapper

@Repository
class EventRepository(
    private val eventMapper: EventMapper,
) {
    fun findById(id: UlidId): Result<Event> = runCatching {
        val eventDto = eventMapper.findById(id.toByteArray())
        if (eventDto == null) {
            throw NotFoundException("Event($id) not found")
        }
        Event.of(eventDto)
    }

    fun createEvent(name: String): Result<Event> = runCatching {
        val ulidId = UlidId.new()

        val insertCount = eventMapper.insert(EventDto(ulidId.toByteArray(), name))
        if (insertCount < 1) {
            throw TransactionException("Create event failed")
        }
        return findById(ulidId)
    }

    fun listEvents(): Result<List<Event>> = runCatching {
        eventMapper.select().map(Event::of)
    }

    fun deleteEvent(id: UlidId): Result<Unit> = runCatching {
        val isSuccess = eventMapper.deleteById(id.toByteArray())
        if (!isSuccess) {
            throw TransactionException("Create event failed")
        }
    }
}
