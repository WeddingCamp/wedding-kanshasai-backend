package wedding.kanshasai.backend.infra.mysql.dto

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IChoice
import java.sql.Timestamp

data class ChoiceDto(
    override var choiceIdentifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    override var quizId: ByteArray = byteArrayOf(),
    override var body: String = "",
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
) : IChoice, IdentifiableDto<StandardIdentifier>(isDeleted) {
    override var identifier: StandardIdentifier
        get() = choiceIdentifier
        set(value) {
            choiceIdentifier = value
        }

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
