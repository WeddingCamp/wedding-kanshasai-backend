package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.EventMapper

@Repository
class EventRepository(
    private val eventMapper: EventMapper,
) : RepositoryBase() {
    override val table = Table.EVENT

    fun findById(id: UlidId): Result<Event> = runCatching {
        val result = findById(eventMapper, id.toStandardIdentifier())
        Event.of(result)
    }

    fun listAll(): Result<List<Event>> = runCatching {
        eventMapper.select().map(Event::of)
    }
}
