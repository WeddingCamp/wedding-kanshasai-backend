package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mapper.QuizMapper

@Repository
class QuizRepository(
    private val quizMapper: QuizMapper,
) {
    fun listByEventId(eventId: UlidId): Result<List<Quiz>> = runCatching {
        val resultList = quizMapper.listByEventId(eventId.toByteArray())
        resultList.map(Quiz::of)
    }
}
