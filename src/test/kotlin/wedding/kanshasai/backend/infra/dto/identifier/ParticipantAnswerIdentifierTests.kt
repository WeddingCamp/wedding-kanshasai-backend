package wedding.kanshasai.backend.infra.dto.identifier

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import wedding.kanshasai.backend.domain.value.UlidId

class ParticipantAnswerIdentifierTests {
    @Test
    fun toString_shouldReturnCorrectFormatText() {
        val participantId = UlidId.new()
        val sessionId = UlidId.new()
        val quizId = UlidId.new()

        val identifier = ParticipantAnswerIdentifier(
            participantId.toByteArray(),
            SessionQuizIdentifier(sessionId.toByteArray(), quizId.toByteArray()),
        )
        assertEquals(
            "ParticipantAnswerIdentifier(participantId='$participantId', SessionQuizIdentifier(sessionId='$sessionId', quizId='$quizId'))",
            identifier.toString(),
        )
    }
}
