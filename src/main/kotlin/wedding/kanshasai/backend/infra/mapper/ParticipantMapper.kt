package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.ParticipantDto

@Mapper
interface ParticipantMapper : MapperCRUDBase<ParticipantDto>
