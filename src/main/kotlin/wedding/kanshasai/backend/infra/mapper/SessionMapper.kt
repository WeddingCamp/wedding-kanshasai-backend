package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.SessionDto

@Mapper
interface SessionMapper {
    fun findById(
        @Param("id") id: ByteArray,
    ): SessionDto?

    fun listSessions(
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<SessionDto>

    fun createSession(
        @Param("id") id: ByteArray,
        @Param("event_id") eventId: ByteArray,
        @Param("name") name: String,
    ): Int

    fun deleteById(
        @Param("id") id: ByteArray,
    ): Boolean
}
