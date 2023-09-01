package wedding.kanshasai.backend.infra.mysql.dto

import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IQuiz
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.ISessionQuiz
import java.sql.Timestamp

data class QuizWithStatusDto(
    override var quizIdentifier: StandardIdentifier = StandardIdentifier(),
    override var sessionQuizIdentifier: SessionQuizIdentifier = SessionQuizIdentifier(),
    override var eventId: ByteArray = byteArrayOf(),
    override var body: String = "",
    override var correctAnswer: String = "",
    override var type: Int = 0,
    override var isCompleted: Boolean = false,
    override var startedAt: Timestamp? = null,
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
    @Deprecated("'sessionId' is deprecated. Please use 'identifier.sessionId' instead.")
    var sessionId: ByteArray = byteArrayOf(),
    @Deprecated("'quizId' is deprecated. Please use 'identifier.quizId' instead.")
    var quizId: ByteArray = byteArrayOf(),
) : IQuiz, ISessionQuiz {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizWithStatusDto

        if (quizIdentifier != other.quizIdentifier) return false
        if (sessionQuizIdentifier != other.sessionQuizIdentifier) return false
        if (!eventId.contentEquals(other.eventId)) return false
        if (body != other.body) return false
        if (correctAnswer != other.correctAnswer) return false
        if (type != other.type) return false
        if (isCompleted != other.isCompleted) return false
        if (startedAt != other.startedAt) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = quizIdentifier.hashCode()
}
