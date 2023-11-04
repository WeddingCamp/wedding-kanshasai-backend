package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier

@Mapper
interface ParticipantAnswerMapper : MapperCRUDBase<ParticipantAnswerIdentifier, ParticipantAnswerDto> {
    fun listBySessionIdAndQuizId(
        @Param("sessionId") sessionId: ByteArray,
        @Param("quizId") quizId: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<ParticipantAnswerDto>

    fun deleteBySessionIdAndQuizId(
        @Param("sessionId") sessionId: ByteArray,
        @Param("quizId") quizId: ByteArray,
    ): Int
}
