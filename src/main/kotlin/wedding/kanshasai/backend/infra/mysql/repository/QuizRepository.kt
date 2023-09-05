package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.EventMapper
import wedding.kanshasai.backend.infra.mysql.mapper.QuizMapper

@Repository
class QuizRepository(
    private val eventMapper: EventMapper,
    private val quizMapper: QuizMapper,
) : RepositoryBase() {
    override val table = Table.QUIZ

    fun findById(id: UlidId): Result<Quiz> = runCatching {
        val result = findById(quizMapper, id.toStandardIdentifier())
        Quiz.of(result)
    }

    fun listByEvent(event: Event): Result<List<Quiz>> = runCatching {
        findById(eventMapper, event.id.toStandardIdentifier(), Table.EVENT)
        quizMapper.listByEventId(event.id.toByteArray()).map(Quiz::of)
    }
}
