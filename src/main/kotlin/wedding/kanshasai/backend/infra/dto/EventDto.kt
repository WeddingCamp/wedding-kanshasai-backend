package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.dto.interfaces.IEvent
import java.sql.Timestamp

data class EventDto(
    override var eventIdentifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    override var name: String = "",
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
) : IEvent, IdentifiableDto<StandardIdentifier>(isDeleted) {
    override var identifier: StandardIdentifier
        get() = eventIdentifier
        set(value) {
            eventIdentifier = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventDto

        if (eventIdentifier != other.eventIdentifier) return false
        if (name != other.name) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = eventIdentifier.hashCode()
}
