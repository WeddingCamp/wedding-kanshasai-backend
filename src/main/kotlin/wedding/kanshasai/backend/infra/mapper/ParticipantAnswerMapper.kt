package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.Mapper
import wedding.kanshasai.backend.infra.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.dto.identifier.ParticipantAnswerIdentifier

@Mapper
interface ParticipantAnswerMapper : MapperCRUDBase<ParticipantAnswerIdentifier, ParticipantAnswerDto>
