package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.ChoiceRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionQuizRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionRepository

@Service
class SessionQuizService(
    private val choiceRepository: ChoiceRepository,
    private val sessionRepository: SessionRepository,
    private val sessionQuizRepository: SessionQuizRepository,
) {
    fun listQuizBySessionId(sessionId: UlidId): Result<List<Triple<Quiz, SessionQuiz, List<Choice>>>> = runCatching {
        sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }
        val sessionQuizList = sessionQuizRepository.listBySession(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION_QUIZ, "sessionId", sessionId, it)
        }
        sessionQuizList.map { (quiz, sessionQuiz) ->
            Triple(
                quiz,
                sessionQuiz,
                choiceRepository.listByQuizId(quiz.id).getOrElse {
                    throw DatabaseException.failedToRetrieve(Table.CHOICE, "quizId", quiz.id, it)
                },
            )
        }
    }
}
