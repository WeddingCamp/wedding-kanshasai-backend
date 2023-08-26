package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.interfaces.ISessionQuiz
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
        fun of(sessionQuizDto: ISessionQuiz): SessionQuiz {
            return SessionQuiz(
                UlidId.of(sessionQuizDto.sessionQuizIdentifier.sessionId),
                UlidId.of(sessionQuizDto.sessionQuizIdentifier.quizId),
                sessionQuizDto.isCompleted,
                sessionQuizDto.startedAt,
                sessionQuizDto.isDeleted,
                sessionQuizDto.createdAt,
                sessionQuizDto.updatedAt,
            )
        }
    }
}
