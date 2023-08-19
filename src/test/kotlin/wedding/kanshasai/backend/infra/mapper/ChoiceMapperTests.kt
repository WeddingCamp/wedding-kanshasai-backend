package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.ChoiceDto

@WeddingKanshasaiSpringBootTest
class ChoiceMapperTests : MapperCRUDTest<ChoiceMapper, ChoiceDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList() = (0..9).map {
        val eventDto = testTool.createAndInsertEventDto()

        (0..2).map {
            val quizDto = testTool.createAndInsertQuizDto(eventDto)
            (1..4).map {
                testTool.createChoiceDto(quizDto)
            }
        }.flatten()
    }.flatten()
}
