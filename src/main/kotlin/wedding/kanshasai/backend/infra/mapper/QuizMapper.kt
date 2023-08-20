package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@Mapper
interface QuizMapper : MapperCRUDBase<StandardIdentifier, QuizDto> {
    fun listByEventId(
        @Param("id") id: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<QuizDto>
}
