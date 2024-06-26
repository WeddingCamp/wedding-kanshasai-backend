package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IParticipantAnswer
import java.sql.Timestamp

class ParticipantAnswer private constructor(
    val sessionId: UlidId,
    val quizId: UlidId,
    val participantId: UlidId,
    val answer: String,
    var time: Float,
    var isCorrect: Boolean,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun clone(): ParticipantAnswer {
        return ParticipantAnswer(
            sessionId,
            quizId,
            participantId,
            answer,
            time,
            isCorrect,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }

    fun toDto(): ParticipantAnswerDto = ParticipantAnswerDto(
        participantAnswerIdentifier = ParticipantAnswerIdentifier(
            sessionQuizIdentifier = SessionQuizIdentifier(
                sessionId = sessionId.toByteArray(),
                quizId = quizId.toByteArray(),
            ),
            participantId = participantId.toByteArray(),
        ),
        answer = answer,
        time = time,
        isCorrect = isCorrect,
        isDeleted = isDeleted,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        fun of(participantAnswerDto: IParticipantAnswer): ParticipantAnswer {
            return ParticipantAnswer(
                UlidId.of(participantAnswerDto.participantAnswerIdentifier.sessionQuizIdentifier.sessionId),
                UlidId.of(participantAnswerDto.participantAnswerIdentifier.sessionQuizIdentifier.quizId),
                UlidId.of(participantAnswerDto.participantAnswerIdentifier.participantId),
                participantAnswerDto.answer,
                participantAnswerDto.time,
                participantAnswerDto.isCorrect,
                participantAnswerDto.isDeleted,
                participantAnswerDto.createdAt,
                participantAnswerDto.updatedAt,
            )
        }
    }
}
