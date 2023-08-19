package wedding.kanshasai.backend.infra.mapper

import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.EventDto

@WeddingKanshasaiSpringBootTest
class EventMapperTests : MapperCRUDTest<EventMapper, EventDto>() {
    override fun stubDtoList() = (0..9).map {
        val id = UlidId.new()
        EventDto(
            id.toByteArray(),
            "Event_$it",
        )
    }
}
