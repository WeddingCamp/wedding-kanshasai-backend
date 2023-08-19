package wedding.kanshasai.backend.infra.dto

import java.sql.Timestamp

data class ParticipantDto(
    override var id: ByteArray = byteArrayOf(),
    var sessionId: ByteArray = byteArrayOf(),
    var name: String = "",
    var imageId: ByteArray = byteArrayOf(),
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    var session: SessionDto? = null,
) : IdentifiableDto(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantDto

        if (!id.contentEquals(other.id)) return false
        if (!sessionId.contentEquals(other.sessionId)) return false
        if (name != other.name) return false
        if (!imageId.contentEquals(other.imageId)) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        if (!equals(other)) return false

        other as ParticipantDto

        if (session != other.session) return false

        return true
    }

    override fun hashCode() = id.contentHashCode()
}
