package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@Mapper
interface EventMapper : MapperCRUDBase<StandardIdentifier, EventDto>
