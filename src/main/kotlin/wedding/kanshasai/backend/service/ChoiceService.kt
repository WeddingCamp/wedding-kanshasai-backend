package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.infra.exception.DatabaseException
import wedding.kanshasai.backend.infra.mysql.repository.ChoiceRepository

@Service
class ChoiceService(
    private val choiceRepository: ChoiceRepository,
) {
    fun listByQuiz(quiz: Quiz): Result<List<Choice>> = runCatching {
        try {
            choiceRepository.listByQuiz(quiz).getOrThrow()
        } catch (e: Exception) {
            throw DatabaseException("Failed to list choices by quiz.", e)
        }
    }
}
