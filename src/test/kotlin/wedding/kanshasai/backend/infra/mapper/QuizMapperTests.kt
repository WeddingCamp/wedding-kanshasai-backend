package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.QuizDto

@WeddingKanshasaiSpringBootTest
class QuizMapperTests : MapperCRUDTest<QuizMapper, QuizDto>() {

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
        QuizDto(
            sessionId.toByteArray(),
            eventId.toByteArray(),
            "Quiz_body_$sessionId",
            "Quiz_answer_$sessionId",
            it,
            event = eventDto,
        )
    }
}
