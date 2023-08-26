package wedding.kanshasai.backend.infra.dto.identifier

import wedding.kanshasai.backend.domain.value.UlidId

data class ParticipantAnswerIdentifier(
    var participantId: ByteArray = byteArrayOf(),
    var sessionQuizIdentifier: SessionQuizIdentifier = SessionQuizIdentifier(),
) : DtoIdentifier {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantAnswerIdentifier

        if (!participantId.contentEquals(other.participantId)) return false
        if (sessionQuizIdentifier != other.sessionQuizIdentifier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = participantId.contentHashCode()
        result = 31 * result + sessionQuizIdentifier.hashCode()
        return result
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(participantId='${UlidId.of(participantId)}', $sessionQuizIdentifier)"
    }
}
