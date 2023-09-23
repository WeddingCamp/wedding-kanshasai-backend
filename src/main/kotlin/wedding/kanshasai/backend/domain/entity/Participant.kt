package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantDto
import java.sql.Timestamp

class Participant private constructor(
    val id: UlidId,
    val sessionId: UlidId,
    var name: String,
    var imageId: UlidId?,
    var type: ParticipantType,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun clone(): Participant {
        return Participant(
            id,
            sessionId,
            name,
            imageId,
            type,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }
    companion object {
        fun of(participantDto: ParticipantDto): Participant {
            return Participant(
                UlidId.of(participantDto.participantIdentifier.id),
                UlidId.of(participantDto.sessionId),
                participantDto.name,
                participantDto.imageId?.let(UlidId::of),
                ParticipantType.of(participantDto.type),
                participantDto.isDeleted,
                participantDto.createdAt,
                participantDto.updatedAt,
            )
        }
    }
}
