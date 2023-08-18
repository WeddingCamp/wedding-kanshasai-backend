package wedding.kanshasai.backend.infra.dto

import java.sql.Timestamp

data class SessionDto(
    var id: ByteArray = byteArrayOf(),
    var eventId: ByteArray = byteArrayOf(),
    var name: String = "",
    var stateId: Int = -1,
    var coverScreenId: Int? = null,
    var currentQuizId: ByteArray? = null,
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    var event: EventDto? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SessionDto
        return id.contentEquals(other.id)
    }

    override fun hashCode() = id.contentHashCode()
}
