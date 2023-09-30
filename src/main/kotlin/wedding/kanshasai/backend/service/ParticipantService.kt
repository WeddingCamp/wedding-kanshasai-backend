package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantMapper
import wedding.kanshasai.backend.infra.mysql.repository.ParticipantRepository
import wedding.kanshasai.backend.infra.mysql.repository.SessionRepository

@Service
class ParticipantService(
    private val sessionRepository: SessionRepository,
    private val participantRepository: ParticipantRepository,
) {
    fun findById(participantId: UlidId): Participant {
        return participantRepository.findById(participantId).getOrThrowService()
    }

    fun listParticipantsBySessionId(sessionId: UlidId): List<Participant> {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        return participantRepository.listBySession(session).getOrThrowService()
    }

    fun createParticipant(sessionId: UlidId, name: String, imageId: UlidId?, type: ParticipantType): Participant {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        return participantRepository.createParticipant(session, name, imageId, type).getOrThrowService()
    }

    fun setConnected(participantId: UlidId, isConnected: Boolean) {
        val participant = participantRepository.findById(participantId).getOrThrowService()
        participantRepository.update(
            participant.clone().apply {
                this.isConnected = isConnected
            }
        ).getOrThrowService()
    }
}
