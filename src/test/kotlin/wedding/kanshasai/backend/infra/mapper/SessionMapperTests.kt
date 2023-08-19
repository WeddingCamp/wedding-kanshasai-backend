package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.SessionDto

@WeddingKanshasaiSpringBootTest
class SessionMapperTests : MapperCRUDTest<SessionMapper, SessionDto>() {

    @Autowired
    lateinit var eventMapper: EventMapper

    override fun stubDtoList() = (0..9).map {
        val eventId = UlidId.new()
        val eventDto = EventDto(
            eventId.toByteArray(),
            "Event_$eventId",
        )
        eventMapper.insert(eventDto)
        val sessionId = UlidId.new()
        SessionDto(
            sessionId.toByteArray(),
            eventId.toByteArray(),
            "Session_$sessionId",
            it,
            100,
            null,
            event = eventDto,
        )
    }
}
