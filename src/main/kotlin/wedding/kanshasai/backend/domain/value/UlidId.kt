package wedding.kanshasai.backend.domain.value

import de.huxhorn.sulky.ulid.ULID
import wedding.kanshasai.backend.domain.exception.InvalidUlidFormatException
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

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
        fun of(value: String): Result<UlidId> = runCatching {
            try {
                UlidId(ULID.parseULID(value))
            } catch (e: Exception) {
                throw InvalidUlidFormatException(e.message)
            }
        }

        fun of(value: ByteArray): Result<UlidId> = runCatching {
            try {
                UlidId(ULID.fromBytes(value))
            } catch (e: Exception) {
                throw InvalidUlidFormatException(e.message)
            }
        }

        fun of(value: ULID.Value): Result<UlidId> = runCatching {
            UlidId(value)
        }

        fun new(): UlidId {
            return UlidId(ULID().nextValue())
        }
    }
}
