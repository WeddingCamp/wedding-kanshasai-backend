package wedding.kanshasai.backend.infra.mysql.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.mysql.dto.EventDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

@Mapper
interface EventMapper : MapperCRUDBase<StandardIdentifier, EventDto>
