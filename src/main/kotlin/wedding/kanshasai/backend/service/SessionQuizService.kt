package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.ChoiceRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionQuizRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionRepository
import wedding.kanshasai.backend.service.exception.FailedOperationException

@Service
class SessionQuizService(
    private val choiceRepository: ChoiceRepository,
    private val sessionRepository: SessionRepository,
    private val sessionQuizRepository: SessionQuizRepository,
) {
    fun listQuizBySessionId(sessionId: UlidId): Result<List<Triple<Quiz, SessionQuiz, List<Choice>>>> = runCatching {
        try {
            val session = sessionRepository.findById(sessionId).getOrThrow()
            val sessionQuizList = sessionQuizRepository.listBySession(session).getOrThrow()
            sessionQuizList.map { (quiz, sessionQuiz) ->
                Triple(
                    quiz,
                    sessionQuiz,
                    choiceRepository.listByQuiz(quiz).getOrThrow(),
                )
            }
        } catch (e: Exception) {
            throw FailedOperationException("Failed to list quiz by session id.", e)
        }
    }
}
