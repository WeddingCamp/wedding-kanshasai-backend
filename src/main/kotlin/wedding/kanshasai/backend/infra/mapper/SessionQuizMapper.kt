package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.Mapper
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier

@Mapper
interface SessionQuizMapper : MapperCRUDBase<SessionQuizIdentifier, SessionQuizDto>
