package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.mysql.dto.QuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

@Mapper
interface QuizMapper : MapperCRUDBase<StandardIdentifier, QuizDto> {
    fun listByEventId(
        @Param("id") id: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<QuizDto>
}
