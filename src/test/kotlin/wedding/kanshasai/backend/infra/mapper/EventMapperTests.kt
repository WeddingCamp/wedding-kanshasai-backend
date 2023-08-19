package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@WeddingKanshasaiSpringBootTest
class EventMapperTests : MapperCRUDTest<EventMapper, StandardIdentifier, EventDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<EventDto>, List<EventDto>> {
        val eventDtoList = mutableListOf<EventDto>()
        val updateEventDtoList = mutableListOf<EventDto>()

        repeat(10) {
            val eventDto = testTool.createEventDto()
            eventDtoList.add(eventDto)
            updateEventDtoList.add(
                eventDto.copy().apply {
                    name = testTool.uuid
                    isDeleted = testTool.trueOrFalse
                },
            )
        }
        return Pair(eventDtoList, updateEventDtoList)
    }
}
