package wedding.kanshasai.backend.domain.value

import de.huxhorn.sulky.ulid.ULID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import wedding.kanshasai.backend.exception.InvalidUlidFormatException

class UlidIdTest {
    companion object {
        private const val VALID_ULID_TEXT = "01H820SP9ZVN184B8QD8G8A7HM"
        private const val INVALID_ULID_TEXT = "01H820SP9ZUN184B8QD8G8A7HM"
        private const val NOT_ULID_TEXT = "ulid"

        private val VALID_ULID_BYTE_ARRAY =
            byteArrayOf(
                b(0x01), b(0x8a), b(0x04), b(0x15),
                b(0xd4), b(0xce), b(0xe3), b(0xd1),
                b(0xa3), b(0xd6), b(0x5f), b(0x95),
                b(0x85), b(0x6b), b(0x97), b(0x97),
            )

        private val INVALID_ULID_BYTE_ARRAY =
            byteArrayOf(
                b(0x01), b(0x8a), b(0x04), b(0x15),
                b(0xd4), b(0xce), b(0xe3), b(0xd1),
                b(0xa3), b(0xd6), b(0x5f), b(0x95),
                b(0x85), b(0x6b), b(0x97), b(0x97),
                b(0x34),
            )

        private fun b(byte: Int) = byte.toByte()
    }

    @Test
    fun new_shouldGenerateValidUlidId() {
        val ulidId = UlidId.new()
        assertDoesNotThrow {
            ULID.parseULID(ulidId.toString())
        }
    }

    @Test
    fun of_shouldSucceedWhenValidUlidTextIsPassed() {
        assertDoesNotThrow {
            UlidId.of(VALID_ULID_TEXT)
        }
    }

    @Test
    fun of_shouldFailWhenInvalidUlidTextIsPassed() {
        assertThrows<InvalidUlidFormatException> {
            UlidId.of(INVALID_ULID_TEXT)
        }
    }

    @Test
    fun of_shouldFailWhenNotUlidTextIsPassed() {
        assertThrows<InvalidUlidFormatException> {
            UlidId.of(NOT_ULID_TEXT)
        }
    }

    @Test
    fun of_shouldSucceedWhenValidUlidByteArrayIsPassed() {
        assertDoesNotThrow {
            UlidId.of(VALID_ULID_BYTE_ARRAY)
        }
    }

    @Test
    fun of_shouldFailWhenInvalidUlidByteArrayIsPassed() {
        assertThrows<InvalidUlidFormatException> {
            UlidId.of(INVALID_ULID_BYTE_ARRAY)
        }
    }

    @Test
    fun of_shouldSucceedWhenValidULIDValueIsPassed() {
        assertDoesNotThrow {
            UlidId.of(ULID().nextValue())
        }
    }

    @Test
    fun toString_shouldConvertToValidUlidText() {
        val ulidId = UlidId.new()
        assertDoesNotThrow {
            ULID.parseULID(ulidId.toString())
        }
    }

    @Test
    fun toByteArray_shouldConvertToValidUlidByteArray() {
        val ulidId = UlidId.new()
        assertDoesNotThrow {
            ULID.fromBytes(ulidId.toByteArray())
        }
    }

    @Test
    fun toULIDValue_shouldConvertToValidULIDValue() {
        val ulidId = UlidId.new()
        assert(ulidId.toString() == ulidId.toULIDValue().toString())
    }
}
