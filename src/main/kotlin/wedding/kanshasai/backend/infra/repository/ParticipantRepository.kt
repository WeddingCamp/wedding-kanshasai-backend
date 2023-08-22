package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mapper.ParticipantMapper

private val TABLE = Table.PARTICIPANT

@Repository
class ParticipantRepository(
    private val participantMapper: ParticipantMapper,
) {
    fun findById(id: UlidId): Result<Participant> = runCatching {
        val result = participantMapper.findById(id.toStandardIdentifier())
        if (result == null) throw NotFoundException.record(TABLE, id, null)
        Participant.of(result)
    }

    fun createParticipant(session: Session, name: String, imageId: UlidId?): Result<Participant> = runCatching {
        val id = UlidId.new()
        val participantDto = ParticipantDto(
            id.toStandardIdentifier(),
            session.id.toByteArray(),
            name,
            imageId?.toByteArray(),
        )
        val result = participantMapper.insert(participantDto)
        if (result != 1) throw DatabaseException.incorrectNumberOfInsert(TABLE, 1, result, null)
        return findById(id)
    }
}
