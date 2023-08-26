package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import java.sql.Timestamp

class SessionQuiz private constructor(
    val sessionId: UlidId,
    val quizId: UlidId,
    val isCompleted: Boolean,
    var startedAt: Timestamp?,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    companion object {
        fun of(sessionQuizDto: SessionQuizDto): SessionQuiz {
            return SessionQuiz(
                UlidId.of(sessionQuizDto.identifier.sessionId),
                UlidId.of(sessionQuizDto.identifier.quizId),
                sessionQuizDto.isCompleted,
                sessionQuizDto.startedAt,
                sessionQuizDto.isDeleted,
                sessionQuizDto.createdAt,
                sessionQuizDto.updatedAt,
            )
        }
    }
}
