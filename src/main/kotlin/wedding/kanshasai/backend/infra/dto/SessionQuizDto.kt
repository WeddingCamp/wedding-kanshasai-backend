package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import java.sql.Timestamp

data class SessionQuizDto(
    override var identifier: SessionQuizIdentifier = SessionQuizIdentifier(),
    var isCompleted: Boolean = false,
    var startedAt: Timestamp? = null,
    override var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    @Deprecated("'sessionId' is deprecated. Please use 'identifier.sessionId' instead.")
    var sessionId: ByteArray = byteArrayOf(),
    @Deprecated("'quizId' is deprecated. Please use 'identifier.quizId' instead.")
    var quizId: ByteArray = byteArrayOf(),
) : IdentifiableDto<SessionQuizIdentifier>(identifier, isDeleted) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionQuizDto

        if (identifier != other.identifier) return false
        if (isCompleted != other.isCompleted) return false
        if (startedAt != other.startedAt) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
