package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

@Mapper
interface ParticipantMapper : MapperCRUDBase<StandardIdentifier, ParticipantDto> {
    fun listBySessionId(
        @Param("id") id: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<ParticipantDto>
}
