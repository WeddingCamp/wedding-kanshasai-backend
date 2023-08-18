package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.EventDto

@Mapper
interface EventMapper {
    fun findById(
        @Param("id") id: ByteArray,
    ): EventDto?

    fun createEvent(
        @Param("id") id: ByteArray,
        @Param("name") name: String,
    ): Int

    fun listEvents(
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<EventDto>

    fun deleteById(
        @Param("id") id: ByteArray,
    ): Boolean
}
