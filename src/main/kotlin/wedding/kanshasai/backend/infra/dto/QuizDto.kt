package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import java.sql.Timestamp

data class QuizDto(
    override var identifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    var eventId: ByteArray = byteArrayOf(),
    var body: String = "",
    var correctAnswer: String = "",
    var type: Int = 0,
    override var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) : IdentifiableDto<StandardIdentifier>(identifier, isDeleted) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizDto

        if (identifier != other.identifier) return false
        if (!eventId.contentEquals(other.eventId)) return false
        if (body != other.body) return false
        if (correctAnswer != other.correctAnswer) return false
        if (type != other.type) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = identifier.hashCode()
}
