package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import java.sql.Timestamp

data class EventDto(
    override var identifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    var name: String = "",
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) : IdentifiableDto<StandardIdentifier>(identifier) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventDto

        if (identifier != other.identifier) return false
        if (name != other.name) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        return equals(other)
    }

    override fun hashCode() = identifier.hashCode()
}
