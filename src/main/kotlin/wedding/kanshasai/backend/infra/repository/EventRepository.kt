package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mapper.EventMapper

@Repository
class EventRepository(
    private val eventMapper: EventMapper,
) {
    fun findById(id: UlidId): Result<Event> = runCatching {
        val result = try {
            eventMapper.findById(id.toStandardIdentifier())
        } catch (e: Exception) {
            throw DatabaseException("Failed to retrieve Event.", e)
        }
        if (result == null) throw NotFoundException("Event(id=$id) not found.")

        Event(
            UlidId.of(result.identifier.id).getOrThrow(),
            result.name,
            result.isDeleted,
            result.createdAt,
            result.updatedAt,
        )
    }
}
