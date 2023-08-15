package wedding.kanshasai.backend.entity

import de.huxhorn.sulky.ulid.ULID
import java.sql.Timestamp

data class Event(
    val id: ByteArray = ULID().nextValue().toBytes(),
    var name: String,
    var isDeleted: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp,
) {
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
