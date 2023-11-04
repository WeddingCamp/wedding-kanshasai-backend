package wedding.kanshasai.backend.infra.mysql.dto.identifier

import wedding.kanshasai.backend.domain.value.UlidId

data class SessionQuizIdentifier(
    var sessionId: ByteArray = byteArrayOf(),
    var quizId: ByteArray = byteArrayOf(),
) : DtoIdentifier {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionQuizIdentifier

        if (!sessionId.contentEquals(other.sessionId)) return false
        if (!quizId.contentEquals(other.quizId)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sessionId.contentHashCode()
        result = 31 * result + quizId.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(sessionId='${UlidId.of(sessionId)}', quizId='${UlidId.of(quizId)}')"
    }
}
