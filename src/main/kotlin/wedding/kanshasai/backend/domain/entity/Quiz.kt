package wedding.kanshasai.backend.domain.entity

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import wedding.kanshasai.backend.domain.value.QuizType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IQuiz
import java.sql.Timestamp

class Quiz private constructor(
    val id: UlidId,
    var eventId: UlidId,
    var body: String,
    var correctAnswer: String,
    var type: QuizType,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun getCorrectAnswer(objectMapper: ObjectMapper): GenericAnswer {
        return Jackson2JsonRedisSerializer(objectMapper, GenericAnswer::class.java).deserialize(correctAnswer.toByteArray())
    }

    companion object {
        fun of(quizDto: IQuiz): Quiz {
            return Quiz(
                UlidId.of(quizDto.quizIdentifier.id),
                UlidId.of(quizDto.eventId),
                quizDto.body,
                quizDto.correctAnswer,
                QuizType.of(quizDto.type),
                quizDto.isDeleted,
                quizDto.createdAt,
                quizDto.updatedAt,
            )
        }
    }
}
