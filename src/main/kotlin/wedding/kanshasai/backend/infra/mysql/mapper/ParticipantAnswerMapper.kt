package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.Mapper
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier

@Mapper
interface ParticipantAnswerMapper : MapperCRUDBase<ParticipantAnswerIdentifier, ParticipantAnswerDto>
