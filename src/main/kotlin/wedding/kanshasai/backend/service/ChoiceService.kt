package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.infra.mysql.repository.ChoiceRepository

val TABLE = Table.CHOICE

@Service
class ChoiceService(
    private val choiceRepository: ChoiceRepository,
) {
    fun listByQuiz(quiz: Quiz): Result<List<Choice>> = runCatching {
        choiceRepository.listByQuizId(quiz.id).getOrElse {
            throw DatabaseException.failedToRetrieve(TABLE, it)
        }
    }
}
