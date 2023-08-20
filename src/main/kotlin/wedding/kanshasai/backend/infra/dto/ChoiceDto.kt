package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier
import java.sql.Timestamp

data class ChoiceDto(
    override var identifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    var quizId: ByteArray = byteArrayOf(),
    var body: String = "",
    override var isDeleted: Boolean = false,
    var createdAt: Timestamp = Timestamp(0),
    var updatedAt: Timestamp = Timestamp(0),
) : IdentifiableDto<StandardIdentifier>(identifier, isDeleted) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChoiceDto

        if (identifier != other.identifier) return false
        if (!quizId.contentEquals(other.quizId)) return false
        if (body != other.body) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = identifier.hashCode()
}
