package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import java.sql.Timestamp

data class SessionDto(
    override var identifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    var eventId: ByteArray = byteArrayOf(),
    var name: String = "",
    var stateId: Int = -1,
    var coverScreenId: Int? = null,
    var currentQuizId: ByteArray? = null,
    override var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    var event: EventDto? = null,
) : IdentifiableDto<StandardIdentifier>(identifier, isDeleted) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionDto

        if (identifier != other.identifier) return false
        if (!eventId.contentEquals(other.eventId)) return false
        if (name != other.name) return false
        if (stateId != other.stateId) return false
        if (coverScreenId != other.coverScreenId) return false
        if (currentQuizId != null) {
            if (other.currentQuizId == null) return false
            if (!currentQuizId.contentEquals(other.currentQuizId)) return false
        } else if (other.currentQuizId != null) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        if (!equals(other)) return false

        other as SessionDto

        if (event != other.event) return false

        return true
    }

    override fun hashCode() = identifier.hashCode()
}
