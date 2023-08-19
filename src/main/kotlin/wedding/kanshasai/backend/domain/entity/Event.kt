package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.EventDto
import java.sql.Timestamp

class Event(
    val id: UlidId,
    var name: String,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    companion object {
        fun of(eventDto: EventDto): Event {
            return Event(
                UlidId.of(eventDto.identifier.id),
                eventDto.name,
                eventDto.isDeleted,
                eventDto.createdAt,
                eventDto.updatedAt,
            )
        }
    }
}
