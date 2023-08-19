package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.ParticipantAnswerIdentifier
import java.sql.Timestamp

data class ParticipantAnswerDto(
    override var identifier: ParticipantAnswerIdentifier = ParticipantAnswerIdentifier(),
    var answer: String = "",
    var time: Float = 0f,
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    var sessionQuiz: SessionQuizDto? = null,
    var participant: ParticipantDto? = null,
    @Deprecated("'participantId' is deprecated. Please use 'identifier.participantId' instead.")
    var participantId: ByteArray = byteArrayOf(),
    @Deprecated("'sessionId' is deprecated. Please use 'identifier.sessionId' instead.")
    var sessionId: ByteArray = byteArrayOf(),
    @Deprecated("'quizId' is deprecated. Please use 'identifier.quizId' instead.")
    var quizId: ByteArray = byteArrayOf(),
) : IdentifiableDto<ParticipantAnswerIdentifier>(identifier) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantAnswerDto

        if (identifier != other.identifier) return false
        if (answer != other.answer) return false
        if (time != other.time) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        if (!equals(other)) return false

        other as ParticipantAnswerDto

        if (sessionQuiz != other.sessionQuiz) return false
        if (participant != other.participant) return false

        return true
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
