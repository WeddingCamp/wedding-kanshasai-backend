package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mapper.ParticipantMapper
import wedding.kanshasai.backend.infra.mapper.SessionMapper

private val TABLE = Table.PARTICIPANT

@Repository
class ParticipantRepository(
    private val sessionMapper: SessionMapper,
    private val participantMapper: ParticipantMapper,
) {
    fun findById(id: UlidId): Result<Participant> = runCatching {
        val result = participantMapper.findById(id.toStandardIdentifier())
        if (result == null) throw NotFoundException.record(TABLE, id, null)
        Participant.of(result)
    }

    fun listBySessionId(sessionId: UlidId): Result<List<Participant>> = runCatching {
        val session = sessionMapper.findById(sessionId.toStandardIdentifier())
        if (session == null) throw NotFoundException.record(Table.SESSION, sessionId, null)

        val resultList = participantMapper.listBySessionId(sessionId.toByteArray())
        resultList.map(Participant::of)
    }

    fun createParticipant(session: Session, name: String, imageId: UlidId?): Result<Participant> = runCatching {
        if (name.isEmpty()) throw InvalidArgumentException.empty("name")
        if (sessionMapper.findById(session.id.toStandardIdentifier()) == null) {
            throw NotFoundException.record(Table.SESSION, session.id, null)
        }
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
