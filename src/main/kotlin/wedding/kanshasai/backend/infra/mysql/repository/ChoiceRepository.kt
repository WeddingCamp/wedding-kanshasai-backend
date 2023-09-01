package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.ChoiceMapper
import wedding.kanshasai.backend.infra.mysql.mapper.QuizMapper

@Repository
class ChoiceRepository(
    private val choiceMapper: ChoiceMapper,
    private val quizMapper: QuizMapper,
) {
    fun listByQuizId(quizId: UlidId): Result<List<Choice>> = runCatching {
        val quiz = quizMapper.findById(quizId.toStandardIdentifier())
        if (quiz == null) throw NotFoundException.record(Table.QUIZ, quizId, null)

        val resultList = choiceMapper.listByQuizId(quizId.toByteArray())
        resultList.map(Choice::of)
    }
}
