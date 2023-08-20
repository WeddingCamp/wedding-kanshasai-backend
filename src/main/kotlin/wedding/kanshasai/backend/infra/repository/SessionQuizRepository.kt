package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
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
        val quizDtoList = quizList.map { quiz ->
            SessionQuizDto(
                SessionQuizIdentifier(session.id.toByteArray(), quiz.id.toByteArray()),
            )
        }
        val result = sessionQuizMapper.insertAll(quizDtoList)
        if (quizList.size != result) {
            throw DatabaseException.incorrectNumberOfInsert(Table.SESSION_QUIZ, quizList.size, result, null)
        }
    }
}
