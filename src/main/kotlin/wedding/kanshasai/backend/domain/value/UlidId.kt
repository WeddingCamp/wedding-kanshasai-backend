package wedding.kanshasai.backend.domain.value

import de.huxhorn.sulky.ulid.ULID
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.lang.Exception

class UlidId private constructor(private val value: ULID.Value) {
    override fun toString(): String {
        return value.toString()
    }

    fun toByteArray(): ByteArray {
        return value.toBytes()
    }

    fun toULIDValue(): ULID.Value {
        return value
    }

    fun toStandardIdentifier(): StandardIdentifier {
        return StandardIdentifier(value.toBytes())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UlidId

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
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
