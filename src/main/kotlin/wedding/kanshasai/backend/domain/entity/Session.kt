package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionDto
import java.sql.Timestamp

class Session private constructor(
    val id: UlidId,
    val eventId: UlidId,
    var name: String,
    var stateId: Int,
    var coverScreenId: Int?,
    var currentQuizId: UlidId?,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    companion object {
        fun of(sessionDto: SessionDto): Session {
            return Session(
                UlidId.of(sessionDto.identifier.id).getOrThrow(),
                UlidId.of(sessionDto.eventId).getOrThrow(),
                sessionDto.name,
                sessionDto.stateId,
                sessionDto.coverScreenId,
                sessionDto.currentQuizId?.let { UlidId.of(it).getOrThrow() },
                sessionDto.isDeleted,
                sessionDto.createdAt,
                sessionDto.updatedAt,
            )
        }
    }
}
