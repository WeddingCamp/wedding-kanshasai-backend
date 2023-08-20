package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mapper.SessionQuizMapper

@Repository
class SessionQuizRepository(
    private val sessionQuizMapper: SessionQuizMapper,
) {
    fun insertAll(session: Session, quizList: List<Quiz>): Result<Unit> = runCatching {
        val result = sessionQuizMapper.insertAll(
            quizList.map { quiz ->
                SessionQuizDto(
                    SessionQuizIdentifier(session.id.toByteArray(), quiz.id.toByteArray()),
                )
            },
        )
        if (quizList.size != result) {
            throw DatabaseException("Number of insertions($result) does not match number of quizzes(${quizList.size}).")
        }
    }
}
