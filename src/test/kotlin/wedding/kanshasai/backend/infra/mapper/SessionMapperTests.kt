package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@WeddingKanshasaiSpringBootTest
class SessionMapperTests : MapperCRUDTest<SessionMapper, StandardIdentifier, SessionDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<SessionDto>, List<SessionDto>> {
        val sessionDtoList = mutableListOf<SessionDto>()
        val updateSessionDtoList = mutableListOf<SessionDto>()

        val eventDtoList = mutableListOf<EventDto>()

        repeat(10) {
            val eventDto = testTool.createAndInsertEventDto()
            eventDtoList.add(eventDto)
            repeat(3) {
                val sessionDto = testTool.createSessionDto(eventDto)
                sessionDtoList.add(sessionDto)
                updateSessionDtoList.add(
                    sessionDto.copy().apply {
                        name = testTool.uuid
                        stateId = (0..100).random()
                        coverScreenId = testTool.maybeNull((0..100).random())
                        currentQuizId = testTool.maybeNull(UlidId.new().toByteArray())
                        isDeleted = testTool.trueOrFalse
                        eventId = eventDtoList.random().eventIdentifier.id
                    },
                )
            }
        }
        return Pair(sessionDtoList, updateSessionDtoList)
    }
}
