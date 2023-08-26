package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

@WeddingKanshasaiSpringBootTest
class QuizMapperTests : MapperCRUDTest<QuizMapper, StandardIdentifier, QuizDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<QuizDto>, List<QuizDto>> {
        val quizDtoList = mutableListOf<QuizDto>()
        val updateQuizDtoList = mutableListOf<QuizDto>()

        val eventDtoList = mutableListOf<EventDto>()

        repeat(10) {
            val eventDto = testTool.createAndInsertEventDto()
            eventDtoList.add(eventDto)
            repeat(3) {
                val quizDto = testTool.createQuizDto(eventDto)
                quizDtoList.add(quizDto)
                updateQuizDtoList.add(
                    quizDto.copy().apply {
                        body = testTool.uuid
                        correctAnswer = testTool.uuid
                        type = (0..100).random()
                        isDeleted = testTool.trueOrFalse
                        eventId = eventDtoList.random().eventIdentifier.id
                    },
                )
            }
        }
        return Pair(quizDtoList, updateQuizDtoList)
    }
}
