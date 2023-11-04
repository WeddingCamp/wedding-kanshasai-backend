package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.ChoiceRepository
import wedding.kanshasai.backend.infra.mysql.repository.QuizRepository

@Service
class ChoiceService(
    private val quizRepository: QuizRepository,
    private val choiceRepository: ChoiceRepository,
) {
    fun listByQuizId(quizId: UlidId): List<Choice> {
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        return choiceRepository.listByQuiz(quiz).getOrThrowService()
    }
}
