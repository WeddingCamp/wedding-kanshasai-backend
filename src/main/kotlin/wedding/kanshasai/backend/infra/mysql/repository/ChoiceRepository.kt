package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.infra.mysql.mapper.ChoiceMapper

@Repository
class ChoiceRepository(
    private val choiceMapper: ChoiceMapper,
) : RepositoryBase() {
    override val table = Table.CHOICE

    fun listByQuiz(quiz: Quiz): Result<List<Choice>> = runCatching {
        choiceMapper.listByQuizId(quiz.id.toByteArray()).map(Choice::of)
    }
}
