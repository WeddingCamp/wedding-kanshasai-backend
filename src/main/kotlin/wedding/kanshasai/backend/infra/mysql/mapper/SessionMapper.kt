package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

@Mapper
interface SessionMapper : MapperCRUDBase<StandardIdentifier, SessionDto>
