package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.CoverScreenType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import java.sql.Timestamp

class Session private constructor(
    val id: UlidId,
    val eventId: UlidId,
    var name: String,
    var stateId: Int,
    var coverScreen: CoverScreenType?,
    var currentQuizId: UlidId?,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun toDto(): SessionDto = SessionDto(
        id.toStandardIdentifier(),
        eventId.toByteArray(),
        name,
        stateId,
        coverScreen?.toNumber(),
        currentQuizId?.toByteArray(),
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
                sessionDto.stateId,
                sessionDto.coverScreenId?.let(CoverScreenType::of),
                sessionDto.currentQuizId?.let(UlidId::of),
                sessionDto.isDeleted,
                sessionDto.createdAt,
                sessionDto.updatedAt,
            )
        }
    }
}
