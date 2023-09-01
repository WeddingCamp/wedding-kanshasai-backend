package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.EventMapper

private val TABLE = Table.EVENT

@Repository
class EventRepository(
    private val eventMapper: EventMapper,
) {
    fun findById(id: UlidId): Result<Event> = runCatching {
        val result = eventMapper.findById(id.toStandardIdentifier())
        if (result == null) throw NotFoundException.record(TABLE, id, null)
        Event.of(result)
    }

    fun listAll(): Result<List<Event>> = runCatching {
        val eventList = eventMapper.select()
        eventList.map(Event::of)
    }
}
