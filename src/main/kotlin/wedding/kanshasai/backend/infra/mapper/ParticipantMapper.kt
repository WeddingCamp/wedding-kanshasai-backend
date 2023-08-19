package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.ParticipantDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@Mapper
interface ParticipantMapper : MapperCRUDBase<StandardIdentifier, ParticipantDto>
