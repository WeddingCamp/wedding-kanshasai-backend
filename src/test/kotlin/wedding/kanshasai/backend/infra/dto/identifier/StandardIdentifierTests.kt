package wedding.kanshasai.backend.infra.dto.identifier

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import wedding.kanshasai.backend.domain.value.UlidId

class StandardIdentifierTests {
    @Test
    fun toString_shouldReturnCorrectFormatText() {
        val id = UlidId.new()

        val identifier = StandardIdentifier(id.toByteArray())
        assertEquals("StandardIdentifier(id='$id')", identifier.toString())
    }
}
