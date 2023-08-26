package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.ParticipantAnswerIdentifier
import wedding.kanshasai.backend.infra.dto.interfaces.IParticipantAnswer
import java.sql.Timestamp

data class ParticipantAnswerDto(
    override var participantAnswerIdentifier: ParticipantAnswerIdentifier = ParticipantAnswerIdentifier(),
    override var answer: String = "",
    override var time: Float? = null,
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
    @Deprecated("'participantId' is deprecated. Please use 'identifier.participantId' instead.")
    var participantId: ByteArray = byteArrayOf(),
    @Deprecated("'sessionId' is deprecated. Please use 'identifier.sessionId' instead.")
    var sessionId: ByteArray = byteArrayOf(),
    @Deprecated("'quizId' is deprecated. Please use 'identifier.quizId' instead.")
    var quizId: ByteArray = byteArrayOf(),
) : IParticipantAnswer, IdentifiableDto<ParticipantAnswerIdentifier>(isDeleted) {
    override var identifier: ParticipantAnswerIdentifier
        get() = participantAnswerIdentifier
        set(value) {
            participantAnswerIdentifier = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantAnswerDto

        if (participantAnswerIdentifier != other.participantAnswerIdentifier) return false
        if (answer != other.answer) return false
        if (time != other.time) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode(): Int {
        return participantAnswerIdentifier.hashCode()
    }
}
