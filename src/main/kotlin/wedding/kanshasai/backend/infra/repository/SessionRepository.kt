package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.mapper.SessionMapper

@Repository
class SessionRepository(
    private val sessionMapper: SessionMapper,
) {
    fun findById(id: UlidId): Result<Session> = runCatching {
        val result = sessionMapper.findById(id.toStandardIdentifier())
        if (result == null) {
            throw NotFoundException("Session(id=$id) not found.")
        }
        Session.of(result)
    }

    fun createSession(event: Event, name: String): Result<Session> = runCatching {
        val id = UlidId.new()
        val sessionDto = SessionDto(
            id.toStandardIdentifier(),
            event.id.toByteArray(),
            name,
        )
        val result = sessionMapper.insert(sessionDto)
        if (result != 1) throw DatabaseException("Failed to insert session.")
        return findById(id)
    }
}
