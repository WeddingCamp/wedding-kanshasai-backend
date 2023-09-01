package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.ParticipantRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionRepository

@Service
class ParticipantService(
    private val sessionRepository: SessionRepository,
    private val participantRepository: ParticipantRepository,
) {
    fun listParticipantsBySessionId(sessionId: UlidId): Result<List<Participant>> = runCatching {
        sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }
        participantRepository.listBySessionId(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.PARTICIPANT, "sessionId", sessionId, it)
        }
    }
    fun createParticipant(sessionId: UlidId, name: String, imageId: UlidId?): Result<Participant> = runCatching {
        val session = sessionRepository.findById(sessionId).getOrElse {
            throw DatabaseException.failedToRetrieve(Table.SESSION, sessionId, it)
        }
        participantRepository.createParticipant(session, name, imageId).getOrElse {
            throw DatabaseException.failedToInsert(Table.PARTICIPANT, it)
        }
    }
}
