package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.*
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.infra.exception.DatabaseNotFoundException
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantAnswerMapper

@Repository
class ParticipantAnswerRepository(
    private val participantAnswerMapper: ParticipantAnswerMapper,
) : RepositoryBase() {
    override val table = Table.PARTICIPANT_ANSWER

    fun find(session: Session, quiz: Quiz, participant: Participant): Result<ParticipantAnswer> = runCatching {
        val id =
            ParticipantAnswerIdentifier(
                sessionQuizIdentifier = SessionQuizIdentifier(
                    sessionId = session.id.toByteArray(),
                    quizId = quiz.id.toByteArray(),
                ),
                participantId = participant.id.toByteArray(),
            )
        val participantAnswerDto = participantAnswerMapper.findById(id)
        if (participantAnswerDto == null) throw DatabaseNotFoundException.retrieve(table, id, null)
        ParticipantAnswer.of(participantAnswerDto)
    }

    fun createParticipantAnswer(
        session: Session,
        quiz: Quiz,
        participant: Participant,
        answer: String,
        time: Float,
    ): Result<ParticipantAnswer> = runCatching {
        if (answer.isEmpty()) throw InvalidArgumentException.empty("answer")
        if (time < 0) throw InvalidArgumentException("time must be positive number.")

        val participantAnswerDto = ParticipantAnswerDto(
            ParticipantAnswerIdentifier(
                sessionQuizIdentifier = SessionQuizIdentifier(
                    sessionId = session.id.toByteArray(),
                    quizId = quiz.id.toByteArray(),
                ),
                participantId = participant.id.toByteArray(),
            ),
            answer,
            time,
        )
        insert(participantAnswerMapper, participantAnswerDto)

        return find(session, quiz, participant)
    }

    fun deleteBySessionQuiz(sessionQuiz: SessionQuiz): Result<Unit> = runCatching {
        participantAnswerMapper.deleteBySessionIdAndQuizId(
            sessionQuiz.sessionId.toByteArray(),
            sessionQuiz.quizId.toByteArray(),
        )
    }

    fun listBySessionQuiz(sessionQuiz: SessionQuiz): Result<List<ParticipantAnswer>> = runCatching {
        participantAnswerMapper.listBySessionIdAndQuizId(
            sessionQuiz.sessionId.toByteArray(),
            sessionQuiz.quizId.toByteArray(),
        ).map(ParticipantAnswer::of)
    }

    fun update(participantAnswer: ParticipantAnswer): Result<Unit> = runCatching {
        update(participantAnswerMapper, participantAnswer.toDto())
    }

    fun updateAll(
        isCorrect: Boolean,
        participantAnswerList: List<ParticipantAnswer>,
        includeDeleted: Boolean = false,
    ): Result<Unit> = runCatching {
        if (participantAnswerList.isEmpty()) return@runCatching
        val sessionId = participantAnswerList.first().sessionId
        val quizId = participantAnswerList.first().quizId

        participantAnswerMapper.updateAll(
            isCorrect,
            participantAnswerList.map { it.participantId.toByteArray() },
            sessionId.toByteArray(),
            quizId.toByteArray(),
            includeDeleted,
        )
    }
}
