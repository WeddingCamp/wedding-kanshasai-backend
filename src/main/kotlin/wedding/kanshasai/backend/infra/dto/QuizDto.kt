package wedding.kanshasai.backend.infra.dto

import java.sql.Timestamp

data class QuizDto(
    override var id: ByteArray = byteArrayOf(),
    var eventId: ByteArray = byteArrayOf(),
    var body: String = "",
    var correctAnswer: String = "",
    var type: Int = 0,
    var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
    var event: EventDto? = null,
) : IdentifiableDto(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizDto

        if (!id.contentEquals(other.id)) return false
        if (!eventId.contentEquals(other.eventId)) return false
        if (body != other.body) return false
        if (correctAnswer != other.correctAnswer) return false
        if (type != other.type) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun strictEquals(other: Any?): Boolean {
        if (!equals(other)) return false

        other as QuizDto

        if (event != other.event) return false

        return true
    }

    override fun hashCode() = id.contentHashCode()
}
