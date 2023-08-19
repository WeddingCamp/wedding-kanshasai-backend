package wedding.kanshasai.backend.infra.dto.identifier

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
}
