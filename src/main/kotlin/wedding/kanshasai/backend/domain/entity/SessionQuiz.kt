package wedding.kanshasai.backend.domain.entity

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
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun toDto(): SessionQuizDto = SessionQuizDto(
        SessionQuizIdentifier(sessionId.toByteArray(), quizId.toByteArray()),
        isCompleted,
        startedAt,
        isDeleted,
        createdAt,
        updatedAt,
    )

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
