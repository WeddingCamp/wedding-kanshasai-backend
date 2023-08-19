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
        val quizDtoList = (1..10).map {
            testTool.createAndInsertQuizDto(eventDto)
        }
        (0..2).map {
            val sessionDto = testTool.createAndInsertSessionDto(eventDto)
            quizDtoList.map {
                testTool.createSessionQuizDto(sessionDto, it)
            }
        }.flatten()
    }.flatten()
}
