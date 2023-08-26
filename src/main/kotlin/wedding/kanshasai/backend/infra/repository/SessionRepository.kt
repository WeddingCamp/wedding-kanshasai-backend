package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.CoverScreenType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.mapper.EventMapper
import wedding.kanshasai.backend.infra.mapper.SessionMapper

private val TABLE = Table.SESSION

@Repository
class SessionRepository(
    private val eventMapper: EventMapper,
    private val sessionMapper: SessionMapper,
) {
    fun findById(id: UlidId): Result<Session> = runCatching {
        val result = sessionMapper.findById(id.toStandardIdentifier())
        if (result == null) throw NotFoundException.record(TABLE, id, null)
        Session.of(result)
    }

    fun createSession(event: Event, name: String): Result<Session> = runCatching {
        if (name.isEmpty()) throw InvalidArgumentException.empty("name")
        if (eventMapper.findById(event.id.toStandardIdentifier()) == null) {
            throw NotFoundException.record(Table.EVENT, event.id, null)
        }
        val id = UlidId.new()
        val sessionDto = SessionDto(
            id.toStandardIdentifier(),
            event.id.toByteArray(),
            name,
        )
        val result = sessionMapper.insert(sessionDto)
        if (result != 1) throw DatabaseException.incorrectNumberOfInsert(TABLE, 1, result, null)
        return findById(id)
    }

    fun updateCoverScreen(session: Session, coverScreenType: CoverScreenType): Result<Unit> = runCatching {
        val isSucceed = sessionMapper.update(session.toDto().copy(coverScreenId = coverScreenType.value))
        if (!isSucceed) throw DatabaseException.failedToUpdate(TABLE, session.id, null)
    }
}
