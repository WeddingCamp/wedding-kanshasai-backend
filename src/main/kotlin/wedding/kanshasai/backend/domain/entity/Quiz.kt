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
    fun clone(): Quiz {
        return Quiz(
            id,
            eventId,
            body,
            correctAnswer,
            type,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }

    fun getCorrectAnswer(): GenericAnswer {
        return Jackson2JsonRedisSerializer(ObjectMapper(), GenericAnswer::class.java).deserialize(correctAnswer.toByteArray())
    }

    fun isCorrectAnswer(sessionQuiz: SessionQuiz, answer: String): Boolean {
        return when (type) {
            QuizType.FOUR_CHOICES_QUIZ -> getCorrectAnswer().choiceIdList.contains(answer)
            QuizType.SORT_IMAGE_QUIZ -> getCorrectAnswer().choiceIdList.contains(answer)
            QuizType.REALTIME_FOUR_CHOICE_QUIZ -> sessionQuiz.getCorrectAnswer().choiceIdList.contains(answer)
            else -> correctAnswer == answer
        }
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
