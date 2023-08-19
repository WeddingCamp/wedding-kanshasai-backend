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

    override fun stubDtoList() = (0..9).map {
        testTool.createEventDto()
    }
}
