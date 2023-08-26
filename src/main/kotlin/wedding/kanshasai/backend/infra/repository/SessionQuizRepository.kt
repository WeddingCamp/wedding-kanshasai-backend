package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mapper.SessionMapper
import wedding.kanshasai.backend.infra.mapper.SessionQuizMapper

@Repository
class SessionQuizRepository(
    private val sessionMapper: SessionMapper,
    private val sessionQuizMapper: SessionQuizMapper,
) {
    fun findById(session: Session, quiz: Quiz): Result<SessionQuiz> = runCatching {
        val sessionQuizDto = sessionQuizMapper.findById(
            SessionQuizIdentifier(session.id.toByteArray(), quiz.id.toByteArray()),
        )
        if (sessionQuizDto == null) {
            throw DatabaseException.failedToRetrieve(Table.SESSION_QUIZ, "sessionId", session.id, "quizId", quiz.id, null)
        }
        SessionQuiz.of(sessionQuizDto)
    }

    fun insertQuizList(session: Session, quizList: List<Quiz>): Result<Unit> = runCatching {
        if (sessionMapper.findById(session.id.toStandardIdentifier()) == null) {
            throw DatabaseException.failedToRetrieve(Table.SESSION, session.id, null)
        }
        if (quizList.isEmpty()) {
            return@runCatching
        }
        if (quizList.any { it.eventId != session.eventId }) {
//            throw DatabaseException.failedToInsert(Table.SESSION_QUIZ, "eventId", session.eventId, null)
        }
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
