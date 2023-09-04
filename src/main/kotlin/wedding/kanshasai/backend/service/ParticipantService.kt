package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.ParticipantRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionRepository
import wedding.kanshasai.backend.service.exception.FailedOperationException

@Service
class ParticipantService(
    private val sessionRepository: SessionRepository,
    private val participantRepository: ParticipantRepository,
) {
    fun listParticipantsBySessionId(sessionId: UlidId): Result<List<Participant>> = runCatching {
        try {
            val session = sessionRepository.findById(sessionId).getOrThrow()
            participantRepository.listBySession(session).getOrThrow()
        } catch (e: Exception) {
            throw FailedOperationException("Failed to list participants by session id.", e)
        }
    }
    fun createParticipant(sessionId: UlidId, name: String, imageId: UlidId?): Result<Participant> = runCatching {
        try {
            val session = sessionRepository.findById(sessionId).getOrThrow()
            participantRepository.createParticipant(session, name, imageId).getOrThrow()
        } catch (e: Exception) {
            throw FailedOperationException("Failed to create participant.", e)
        }
    }
}
