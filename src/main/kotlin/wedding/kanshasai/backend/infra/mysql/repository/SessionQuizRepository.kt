package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.infra.mysql.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.SessionQuizMapper

@Repository
class SessionQuizRepository(
    private val sessionQuizMapper: SessionQuizMapper,
) : RepositoryBase() {
    override val table = Table.SESSION_QUIZ

    fun findById(session: Session, quiz: Quiz): Result<SessionQuiz> = runCatching {
        val id = SessionQuizIdentifier(session.id.toByteArray(), quiz.id.toByteArray())
        val result = findById(sessionQuizMapper, id)
        SessionQuiz.of(result)
    }

    fun listBySession(session: Session): Result<List<Pair<Quiz, SessionQuiz>>> = runCatching {
        sessionQuizMapper.listBySessionId(session.id.toByteArray())
            .map {
                Pair(Quiz.of(it), SessionQuiz.of(it))
            }
    }

    fun insertQuizList(session: Session, quizList: List<Quiz>): Result<Unit> = runCatching {
        if (quizList.isEmpty()) return@runCatching
        if (quizList.any { it.eventId != session.eventId }) {
            throw InvalidArgumentException("The argument quizList contains a quiz that is not a child of session.")
        }
        val sessionQuizDtoList = quizList.map { quiz ->
            SessionQuizDto(
                SessionQuizIdentifier(session.id.toByteArray(), quiz.id.toByteArray()),
            )
        }
        insertAll(sessionQuizMapper, sessionQuizDtoList)
    }

    fun update(sessionQuiz: SessionQuiz): Result<Unit> = runCatching {
        update(sessionQuizMapper, sessionQuiz.toDto())
    }
}
