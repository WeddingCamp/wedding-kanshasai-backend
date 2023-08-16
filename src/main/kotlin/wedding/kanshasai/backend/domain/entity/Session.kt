package wedding.kanshasai.backend.domain.entity

import de.huxhorn.sulky.ulid.ULID
import java.sql.Timestamp

data class Session(
    val id: ByteArray = ULID().nextValue().toBytes(),
    val eventId: ByteArray,
    var name: String,
    var sessionStateId: Int = 1,
    var coverScreenId: Int? = null,
    var currentQuizId: ByteArray? = null,
    var isDeleted: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
) {
    companion object {
        fun of(eventId: ByteArray, name: String): Session {
            return Session(eventId = eventId, name = name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Session

        return id.contentEquals(other.id)
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }
}
