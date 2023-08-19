package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.*
import wedding.kanshasai.backend.infra.dto.QuizDto

@Mapper
interface QuizMapper : MapperCRUDBase<QuizDto>
