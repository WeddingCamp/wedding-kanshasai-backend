package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.ChoiceDto
import java.sql.Timestamp

class Choice private constructor(
    val id: UlidId,
    val quizId: UlidId,
    var body: String,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun clone(): Choice {
        return Choice(
            id,
            quizId,
            body,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }

    companion object {
        fun of(choiceDto: ChoiceDto): Choice {
            return Choice(
                UlidId.of(choiceDto.identifier.id),
                UlidId.of(choiceDto.quizId),
                choiceDto.body,
                choiceDto.isDeleted,
                choiceDto.createdAt,
                choiceDto.updatedAt,
            )
        }
    }
}
