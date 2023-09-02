package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.EventMapper
import wedding.kanshasai.backend.infra.mysql.mapper.QuizMapper

private val TABLE = Table.QUIZ

@Repository
class QuizRepository(
    private val eventMapper: EventMapper,
    private val quizMapper: QuizMapper,
) {
    fun findById(id: UlidId): Result<Quiz> = runCatching {
        val result = quizMapper.findById(id.toStandardIdentifier())
        if (result == null) throw NotFoundException.record(TABLE, id, null)
        Quiz.of(result)
    }

    fun listByEventId(eventId: UlidId): Result<List<Quiz>> = runCatching {
        val event = eventMapper.findById(eventId.toStandardIdentifier())
        if (event == null) throw NotFoundException.record(Table.EVENT, eventId, null)

        val resultList = quizMapper.listByEventId(eventId.toByteArray())
        resultList.map(Quiz::of)
    }
}
