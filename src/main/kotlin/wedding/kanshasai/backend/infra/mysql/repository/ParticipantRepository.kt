package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.domain.value.SessionResult
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantMapper

@Repository
class ParticipantRepository(
    private val participantMapper: ParticipantMapper,
) : RepositoryBase() {
    override val table = Table.PARTICIPANT

    fun findById(id: UlidId): Result<Participant> = runCatching {
        val result = findById(participantMapper, id.toStandardIdentifier())
        Participant.of(result)
    }

    fun listBySession(session: Session): Result<List<Participant>> = runCatching {
        participantMapper.listBySessionId(session.id.toByteArray()).map(Participant::of)
    }

    fun listBySessionWithResult(session: Session): Result<List<Pair<Participant, SessionResult>>> = runCatching {
        participantMapper.listBySessionIdWithResult(session.id.toByteArray()).map {
            val participant = Participant.of(it)
            val result = SessionResult(it.score, it.time, it.rank)
            participant to result
        }
    }

    fun createParticipant(session: Session, name: String, imageId: UlidId?, type: ParticipantType): Result<Participant> = runCatching {
        if (name.isEmpty()) throw InvalidArgumentException.empty("name")

        val participantId = UlidId.new()
        val participantDto = ParticipantDto(
            participantId.toStandardIdentifier(),
            session.id.toByteArray(),
            name,
            imageId?.toByteArray(),
            type.toNumber(),
        )
        insert(participantMapper, participantDto)

        return findById(participantId)
    }

    fun update(participant: Participant): Result<Unit> = runCatching {
        update(participantMapper, participant.toDto())
    }
}
