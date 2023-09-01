package wedding.kanshasai.backend.infra.dto.identifier

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier

class SessionQuizIdentifierTests {
    @Test
    fun toString_shouldReturnCorrectFormatText() {
        val sessionId = UlidId.new()
        val quizId = UlidId.new()

        val identifier = SessionQuizIdentifier(sessionId.toByteArray(), quizId.toByteArray())
        assertEquals("SessionQuizIdentifier(sessionId='$sessionId', quizId='$quizId')", identifier.toString())
    }
}
