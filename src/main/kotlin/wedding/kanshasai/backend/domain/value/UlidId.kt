package wedding.kanshasai.backend.domain.value

import de.huxhorn.sulky.ulid.ULID
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import java.lang.Exception

class UlidId private constructor(private val ulid: ULID.Value) {
    override fun toString(): String {
        return ulid.toString()
    }

    fun toByteArray(): ByteArray {
        return ulid.toBytes()
    }

    fun toULIDValue(): ULID.Value {
        return ulid
    }

    fun toStandardIdentifier(): StandardIdentifier {
        return StandardIdentifier(ulid.toBytes())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UlidId

        return ulid == other.ulid
    }

    override fun hashCode(): Int {
        return ulid.hashCode()
    }

    companion object {
        fun of(value: String): UlidId {
            return try {
                UlidId(ULID.parseULID(value))
            } catch (e: Exception) {
                throw InvalidValueException("Invalid ULID format.", e)
            }
        }

        fun of(value: ByteArray): UlidId {
            return try {
                UlidId(ULID.fromBytes(value))
            } catch (e: Exception) {
                throw InvalidValueException("Invalid ULID format.", e)
            }
        }

        fun of(value: ULID.Value): UlidId {
            return try {
                UlidId(value)
            } catch (e: Exception) {
                throw InvalidValueException("Invalid ULID format.", e)
            }
        }

        fun new(): UlidId = UlidId(ULID().nextValue())
    }
}
