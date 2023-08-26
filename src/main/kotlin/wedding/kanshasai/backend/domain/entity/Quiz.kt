package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.QuizDto
import java.sql.Timestamp

class Quiz private constructor(
    val id: UlidId,
    var eventId: UlidId,
    var body: String,
    var correctAnswer: String,
    var type: Int,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    companion object {
        fun of(quizDto: QuizDto): Quiz {
            return Quiz(
                UlidId.of(quizDto.identifier.id),
                UlidId.of(quizDto.eventId),
                quizDto.body,
                quizDto.correctAnswer,
                quizDto.type,
                quizDto.isDeleted,
                quizDto.createdAt,
                quizDto.updatedAt,
            )
        }
    }
}
