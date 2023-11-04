package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.IParticipant
import java.sql.Timestamp

class Participant private constructor(
    val id: UlidId,
    val sessionId: UlidId,
    var name: String,
    var nameRuby: String,
    var imageId: UlidId?,
    var type: ParticipantType,
    var isConnected: Boolean,
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
) {
    fun clone(): Participant {
        return Participant(
            id,
            sessionId,
            name,
            nameRuby,
            imageId,
            type,
            isConnected,
            isDeleted,
            createdAt,
            updatedAt,
        )
    }

    fun toDto(): ParticipantDto = ParticipantDto(
        participantIdentifier = id.toStandardIdentifier(),
        sessionId = sessionId.toByteArray(),
        name = name,
        nameRuby = nameRuby,
        imageId = imageId?.toByteArray(),
        type = type.toNumber(),
        isConnected = isConnected,
        isDeleted = isDeleted,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        fun of(participantDto: IParticipant): Participant {
            return Participant(
                UlidId.of(participantDto.participantIdentifier.id),
                UlidId.of(participantDto.sessionId),
                participantDto.name,
                participantDto.nameRuby,
                participantDto.imageId?.let(UlidId::of),
                ParticipantType.of(participantDto.type),
                participantDto.isConnected,
                participantDto.isDeleted,
                participantDto.createdAt,
                participantDto.updatedAt,
            )
        }
    }
}
