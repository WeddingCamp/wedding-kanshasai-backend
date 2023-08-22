package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.ParticipantDto
import java.sql.Timestamp

class Participant private constructor(
    val id: UlidId,
    val sessionId: UlidId,
    var name: String,
    var imageId: UlidId?,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    companion object {
        fun of(participantDto: ParticipantDto): Participant {
            return Participant(
                UlidId.of(participantDto.identifier.id).getOrThrow(),
                UlidId.of(participantDto.sessionId).getOrThrow(),
                participantDto.name,
                participantDto.imageId?.let { UlidId.of(it).getOrThrow() },
                participantDto.isDeleted,
                participantDto.createdAt,
                participantDto.updatedAt,
            )
        }
    }
}
