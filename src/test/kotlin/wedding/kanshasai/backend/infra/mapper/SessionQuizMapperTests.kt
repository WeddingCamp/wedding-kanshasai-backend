package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.mysql.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.SessionQuizMapper
import java.sql.Timestamp

@WeddingKanshasaiSpringBootTest
class SessionQuizMapperTests : MapperCRUDTest<SessionQuizMapper, SessionQuizIdentifier, SessionQuizDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<SessionQuizDto>, List<SessionQuizDto>> {
        val sessionQuizDtoList = mutableListOf<SessionQuizDto>()
        val updateSessionQuizDtoList = mutableListOf<SessionQuizDto>()

        repeat(10) {
            val eventDto = testTool.createAndInsertEventDto()
            val quizDtoList = (1..10).map {
                testTool.createAndInsertQuizDto(eventDto)
            }
            repeat(3) {
                val sessionDto = testTool.createAndInsertSessionDto(eventDto)
                quizDtoList.forEach {
                    val sessionQuizDto = testTool.createSessionQuizDto(sessionDto, it)
                    sessionQuizDtoList.add(sessionQuizDto)
                    updateSessionQuizDtoList.add(
                        sessionQuizDto.copy().apply {
                            isCompleted = testTool.trueOrFalse
                            startedAt = testTool.maybeNull(Timestamp(System.currentTimeMillis()))
                            isDeleted = testTool.trueOrFalse
                        },
                    )
                }
            }
        }
        return Pair(sessionQuizDtoList, updateSessionQuizDtoList)
    }
}
