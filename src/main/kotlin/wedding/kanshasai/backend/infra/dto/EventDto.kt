package wedding.kanshasai.backend.infra.dto

import java.sql.Timestamp

data class EventDto(
    override var id: ByteArray = byteArrayOf(),
    var name: String = "",
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) : IdentifiableDto(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventDto

        if (!id.contentEquals(other.id)) return false
        if (name != other.name) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        return equals(other)
    }

    override fun hashCode() = id.contentHashCode()
}
