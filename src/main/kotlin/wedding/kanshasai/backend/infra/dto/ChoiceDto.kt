package wedding.kanshasai.backend.infra.dto

import java.sql.Timestamp

data class ChoiceDto(
    override var id: ByteArray = byteArrayOf(),
    var quizId: ByteArray = byteArrayOf(),
    var body: String = "",
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    var quiz: QuizDto? = null,
) : IdentifiableDto(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChoiceDto

        if (!id.contentEquals(other.id)) return false
        if (!quizId.contentEquals(other.quizId)) return false
        if (body != other.body) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        if (!equals(other)) return false

        other as ChoiceDto

        if (quiz != other.quiz) return false

        return true
    }

    override fun hashCode() = id.contentHashCode()
}
