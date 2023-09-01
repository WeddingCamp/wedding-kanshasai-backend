package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.mysql.dto.ChoiceDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

@Mapper
interface ChoiceMapper : MapperCRUDBase<StandardIdentifier, ChoiceDto> {
    fun listByQuizId(
        @Param("quizId") id: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<ChoiceDto>
}
