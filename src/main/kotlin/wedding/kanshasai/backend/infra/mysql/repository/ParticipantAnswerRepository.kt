package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.ParticipantAnswer
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantAnswerMapper

@Repository
class ParticipantAnswerRepository(
    private val participantAnswerMapper: ParticipantAnswerMapper,
) : RepositoryBase() {
    override val table = Table.PARTICIPANT_ANSWER

    fun listBySessionQuiz(sessionQuiz: SessionQuiz): Result<List<ParticipantAnswer>> = runCatching {
        participantAnswerMapper.listBySessionIdAndQuizId(
            sessionQuiz.sessionId.toByteArray(),
            sessionQuiz.quizId.toByteArray(),
        ).map(ParticipantAnswer::of)
    }
}
