package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.SessionDto

@Mapper
interface QuizMapper : MapperCRUDBase<QuizDto>
