package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import wedding.kanshasai.backend.infra.mysql.dto.QuizWithStatusDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier

@Mapper
interface SessionQuizMapper : MapperCRUDBase<SessionQuizIdentifier, SessionQuizDto> {
    fun listBySessionId(
        @Param("sessionId") id: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<QuizWithStatusDto>
}
