package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.EventMapper
import wedding.kanshasai.backend.infra.mysql.mapper.QuizMapper

@Repository
class QuizRepository(
    private val eventMapper: EventMapper,
    private val quizMapper: QuizMapper,
) {
    fun listByEventId(eventId: UlidId): Result<List<Quiz>> = runCatching {
        val event = eventMapper.findById(eventId.toStandardIdentifier())
        if (event == null) throw NotFoundException.record(Table.EVENT, eventId, null)

        val resultList = quizMapper.listByEventId(eventId.toByteArray())
        resultList.map(Quiz::of)
    }
}
