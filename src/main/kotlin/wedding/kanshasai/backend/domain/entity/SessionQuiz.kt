package wedding.kanshasai.backend.domain.entity

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.ISessionQuiz
import java.sql.Timestamp

class SessionQuiz private constructor(
    val sessionId: UlidId,
    val quizId: UlidId,
    var isCompleted: Boolean,
    var startedAt: Timestamp?,
    var sessionQuizCorrectAnswer: String?,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun clone(): SessionQuiz {
        return SessionQuiz(
            sessionId,
            quizId,
            isCompleted,
            startedAt,
            sessionQuizCorrectAnswer,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }

    fun toDto(): SessionQuizDto = SessionQuizDto(
        SessionQuizIdentifier(sessionId.toByteArray(), quizId.toByteArray()),
        isCompleted,
        startedAt,
        sessionQuizCorrectAnswer,
        isDeleted,
        createdAt,
        updatedAt,
    )

    fun getCorrectAnswer(): GenericAnswer {
        val correctAnswer = sessionQuizCorrectAnswer ?: return GenericAnswer()
        return Jackson2JsonRedisSerializer(ObjectMapper(), GenericAnswer::class.java).deserialize(correctAnswer.toByteArray())
    }

    companion object {
        fun of(sessionQuizDto: ISessionQuiz): SessionQuiz {
            return SessionQuiz(
                UlidId.of(sessionQuizDto.sessionQuizIdentifier.sessionId),
                UlidId.of(sessionQuizDto.sessionQuizIdentifier.quizId),
                sessionQuizDto.isCompleted,
                sessionQuizDto.startedAt,
                sessionQuizDto.sessionQuizCorrectAnswer,
                sessionQuizDto.isDeleted,
                sessionQuizDto.createdAt,
                sessionQuizDto.updatedAt,
            )
        }
    }
}
