package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@Mapper
interface SessionMapper : MapperCRUDBase<StandardIdentifier, SessionDto>
