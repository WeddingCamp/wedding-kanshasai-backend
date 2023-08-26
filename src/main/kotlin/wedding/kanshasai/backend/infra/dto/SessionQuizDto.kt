package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.dto.interfaces.ISessionQuiz
import java.sql.Timestamp

data class SessionQuizDto(
    override var sessionQuizIdentifier: SessionQuizIdentifier = SessionQuizIdentifier(),
    override var isCompleted: Boolean = false,
    override var startedAt: Timestamp? = null,
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
    @Deprecated("'sessionId' is deprecated. Please use 'identifier.sessionId' instead.")
    var sessionId: ByteArray = byteArrayOf(),
    @Deprecated("'quizId' is deprecated. Please use 'identifier.quizId' instead.")
    var quizId: ByteArray = byteArrayOf(),
) : ISessionQuiz, IdentifiableDto<SessionQuizIdentifier>(isDeleted) {
    override var identifier: SessionQuizIdentifier
        get() = sessionQuizIdentifier
        set(value) {
            sessionQuizIdentifier = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionQuizDto

        if (sessionQuizIdentifier != other.sessionQuizIdentifier) return false
        if (isCompleted != other.isCompleted) return false
        if (startedAt != other.startedAt) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode(): Int {
        return sessionQuizIdentifier.hashCode()
    }
}
