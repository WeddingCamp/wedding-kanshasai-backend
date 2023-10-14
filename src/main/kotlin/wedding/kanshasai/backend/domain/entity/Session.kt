package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.QuizResultType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import java.sql.Timestamp

class Session private constructor(
    val id: UlidId,
    val eventId: UlidId,
    var name: String,
    var state: SessionState,
    var currentQuizId: UlidId?,
    var currentIntroductionId: Int,
    var currentQuizResult: QuizResultType? = null,
    var isCoverVisible: Boolean,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun clone(): Session {
        return Session(
            id,
            eventId,
            name,
            state,
            currentQuizId,
            currentIntroductionId,
            currentQuizResult,
            isCoverVisible,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }

    fun toDto(): SessionDto = SessionDto(
        id.toStandardIdentifier(),
        eventId.toByteArray(),
        name,
        state.toNumber(),
        currentQuizId?.toByteArray(),
        currentIntroductionId,
        currentQuizResult?.toNumber(),
        isCoverVisible,
        isDeleted,
        createdAt,
        updatedAt,
    )

    companion object {
        fun of(sessionDto: SessionDto): Session {
            return Session(
                UlidId.of(sessionDto.sessionIdentifier.id),
                UlidId.of(sessionDto.eventId),
                sessionDto.name,
                SessionState.of(sessionDto.stateId),
                sessionDto.currentQuizId?.let(UlidId::of),
                sessionDto.currentIntroductionId,
                sessionDto.currentQuizResultId?.let(QuizResultType::of),
                sessionDto.isCoverVisible,
                sessionDto.isDeleted,
                sessionDto.createdAt,
                sessionDto.updatedAt,
            )
        }
    }
}
