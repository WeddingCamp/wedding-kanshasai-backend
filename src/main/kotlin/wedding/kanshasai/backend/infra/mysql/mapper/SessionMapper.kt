package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.mysql.dto.QuizDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

@Mapper
interface SessionMapper : MapperCRUDBase<StandardIdentifier, SessionDto> {
    fun listByEventId(
        @Param("eventId") eventId: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<SessionDto>
}
