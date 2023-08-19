package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@WeddingKanshasaiSpringBootTest
class QuizMapperTests : MapperCRUDTest<QuizMapper, StandardIdentifier, QuizDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList() = (0..9).map {
        val eventDto = testTool.createAndInsertEventDto()
        (0..2).map {
            testTool.createQuizDto(eventDto)
        }
    }.flatten()
}
