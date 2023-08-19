package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.repository.EventRepository

@WeddingKanshasaiSpringBootTest
class SessionMapperTests : MapperCRUDTest<SessionMapper, SessionDto>() {

    @Autowired
    lateinit var eventRepository: EventRepository

    override fun prepareDto() {
        dtoList.addAll(
            (0..9).map {
                val id = UlidId.new()
                val event = eventRepository.createEvent("Event $it").getOrThrow()
                SessionDto(
                    id.toByteArray(),
                    event.id.toByteArray(),
                    "Session_$id",
                    it,
                    100,
                    null,
                )
            },
        )
    }
}
