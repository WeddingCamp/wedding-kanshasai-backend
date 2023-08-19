package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier

@WeddingKanshasaiSpringBootTest
class SessionQuizMapperTests : MapperCRUDTest<SessionQuizMapper, SessionQuizIdentifier, SessionQuizDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList() = (0..9).map {
        val eventDto = testTool.createAndInsertEventDto()
        (0..2).map {
            val sessionDto = testTool.createAndInsertSessionDto(eventDto)
            (1..10).map {
                val quizDto = testTool.createAndInsertQuizDto(eventDto)
                testTool.createSessionQuizDto(sessionDto, quizDto)
            }
        }.flatten()
    }.flatten()
}
