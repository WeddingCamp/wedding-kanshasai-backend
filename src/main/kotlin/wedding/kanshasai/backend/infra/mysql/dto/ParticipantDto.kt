package wedding.kanshasai.backend.infra.mysql.dto

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IParticipant
import java.sql.Timestamp

data class ParticipantDto(
    override var participantIdentifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    override var sessionId: ByteArray = byteArrayOf(),
    override var name: String = "",
    override var imageId: ByteArray? = null,
    override var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) : IParticipant, IdentifiableDto<StandardIdentifier>(isDeleted) {
    override var identifier: StandardIdentifier
        get() = participantIdentifier
        set(value) {
            participantIdentifier = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantDto

        if (participantIdentifier != other.participantIdentifier) return false
        if (!sessionId.contentEquals(other.sessionId)) return false
        if (name != other.name) return false
        if (!imageId.contentEquals(other.imageId)) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = participantIdentifier.hashCode()
}
