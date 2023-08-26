package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mapper.SessionMapper
import wedding.kanshasai.backend.infra.mapper.SessionQuizMapper

private val TABLE = Table.SESSION_QUIZ

@Repository
class SessionQuizRepository(
    private val sessionMapper: SessionMapper,
    private val sessionQuizMapper: SessionQuizMapper,
) {
    fun find(session: Session, quiz: Quiz): Result<SessionQuiz> = runCatching {
        val sessionQuizDto = sessionQuizMapper.findById(
            SessionQuizIdentifier(session.id.toByteArray(), quiz.id.toByteArray()),
        )
        if (sessionQuizDto == null) {
            throw NotFoundException.record(TABLE, "sessionId", session.id, "quizId", quiz.id, null)
        }
        SessionQuiz.of(sessionQuizDto)
    }

    fun listBySession(sessionId: UlidId): Result<List<Pair<Quiz, SessionQuiz>>> = runCatching {
        val session = sessionMapper.findById(sessionId.toStandardIdentifier())
        if (session == null) throw NotFoundException.record(Table.SESSION, sessionId, null)

        val resultList = sessionQuizMapper.listBySessionId(sessionId.toByteArray())
        resultList.map {
            Pair(
                Quiz.of(it),
                SessionQuiz.of(it),
            )
        }
    }

    fun insertQuizList(session: Session, quizList: List<Quiz>): Result<Unit> = runCatching {
        if (sessionMapper.findById(session.id.toStandardIdentifier()) == null) {
            throw NotFoundException.record(TABLE, session.id, null)
        }
        if (quizList.isEmpty()) return@runCatching
        if (quizList.any { it.eventId != session.eventId }) {
            throw InvalidArgumentException("The argument quizList contains a quiz that is not a child of session.")
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
