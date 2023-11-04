package wedding.kanshasai.backend.infra.mysql.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.mapper.SessionMapper

@Repository
class SessionRepository(
    private val sessionMapper: SessionMapper,
) : RepositoryBase() {
    override val table = Table.SESSION

    fun findById(id: UlidId): Result<Session> = runCatching {
        val result = findById(sessionMapper, id.toStandardIdentifier())
        Session.of(result)
    }

    fun createSession(event: Event, name: String): Result<Session> = runCatching {
        if (name.isEmpty()) throw InvalidArgumentException.empty("name")

        val sessionId = UlidId.new()
        val sessionDto = SessionDto(
            sessionId.toStandardIdentifier(),
            event.id.toByteArray(),
            name,
        )
        insert(sessionMapper, sessionDto)

        return findById(sessionId)
    }

    fun listByEvent(event: Event, includeFinished: Boolean): Result<List<Session>> = runCatching {
        sessionMapper.listByEventId(event.id.toByteArray())
            .filter {
                if (includeFinished) {
                    true
                } else {
                    SessionState.of(it.stateId) != SessionState.FINISHED
                }
            }
            .map(Session::of)
    }

    fun update(session: Session): Result<Unit> = runCatching {
        update(sessionMapper, session.toDto())
    }
}
