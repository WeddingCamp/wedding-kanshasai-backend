package wedding.kanshasai.backend.infra.mysql.dto

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IQuiz
import java.sql.Timestamp

data class QuizDto(
    override var quizIdentifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    override var eventId: ByteArray = byteArrayOf(),
    override var body: String = "",
    override var correctAnswer: String = "",
    override var type: Int = 0,
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
) : IQuiz, IdentifiableDto<StandardIdentifier>(isDeleted) {
    override var identifier: StandardIdentifier
        get() = quizIdentifier
        set(value) {
            quizIdentifier = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizDto

        if (quizIdentifier != other.quizIdentifier) return false
        if (!eventId.contentEquals(other.eventId)) return false
        if (body != other.body) return false
        if (correctAnswer != other.correctAnswer) return false
        if (type != other.type) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = quizIdentifier.hashCode()
}
