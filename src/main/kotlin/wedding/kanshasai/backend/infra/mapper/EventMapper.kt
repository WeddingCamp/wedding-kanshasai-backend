package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.EventDto

@Mapper
interface EventMapper : MapperCRUDBase<EventDto>
