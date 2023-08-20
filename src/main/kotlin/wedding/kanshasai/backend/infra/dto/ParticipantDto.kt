package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import java.sql.Timestamp

data class ParticipantDto(
    override var identifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    var sessionId: ByteArray = byteArrayOf(),
    var name: String = "",
    var imageId: ByteArray? = null,
    override var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) : IdentifiableDto<StandardIdentifier>(identifier, isDeleted) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantDto

        if (identifier != other.identifier) return false
        if (!sessionId.contentEquals(other.sessionId)) return false
        if (name != other.name) return false
        if (!imageId.contentEquals(other.imageId)) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = identifier.hashCode()
}
