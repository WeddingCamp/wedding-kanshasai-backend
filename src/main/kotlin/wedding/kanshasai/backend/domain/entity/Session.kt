package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.IntroductionType
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
    var currentIntroduction: IntroductionType? = null,
    var currentQuizResult: QuizResultType? = null,
    var isCoverVisible: Boolean,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun toDto(): SessionDto = SessionDto(
        id.toStandardIdentifier(),
        eventId.toByteArray(),
        name,
        state.toNumber(),
        currentQuizId?.toByteArray(),
        currentIntroduction?.toNumber(),
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
                sessionDto.currentIntroductionId?.let(IntroductionType::of),
                sessionDto.currentQuizResultId?.let(QuizResultType::of),
                sessionDto.isCoverVisible,
                sessionDto.isDeleted,
                sessionDto.createdAt,
                sessionDto.updatedAt,
            )
        }
    }
}
