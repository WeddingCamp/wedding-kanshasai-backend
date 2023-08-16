package wedding.kanshasai.backend.entity

import de.huxhorn.sulky.ulid.ULID
import java.sql.Timestamp

class Event(
    val id: ByteArray = ULID().nextValue().toBytes(),
    var name: String,
    var isDeleted: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
) {
    companion object {
        fun of(name: String): Event {
            return Event(name = name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        return id.contentEquals(other.id)
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }
}
