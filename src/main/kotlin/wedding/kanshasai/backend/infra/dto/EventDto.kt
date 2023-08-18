package wedding.kanshasai.backend.infra.dto

import java.sql.Timestamp

data class EventDto(
    var id: ByteArray = byteArrayOf(),
    var name: String = "",
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EventDto
        return id.contentEquals(other.id)
    }

    override fun hashCode() = id.contentHashCode()
}
