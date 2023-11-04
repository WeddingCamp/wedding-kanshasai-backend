package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.mysql.dto.ChoiceDto
import wedding.kanshasai.backend.infra.mysql.dto.QuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.ChoiceMapper

@WeddingKanshasaiSpringBootTest
class ChoiceMapperTests : MapperCRUDTest<ChoiceMapper, StandardIdentifier, ChoiceDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<ChoiceDto>, List<ChoiceDto>> {
        val choiceDtoList = mutableListOf<ChoiceDto>()
        val updateChoiceDtoList = mutableListOf<ChoiceDto>()

        val quizDtoList = mutableListOf<QuizDto>()

        repeat(10) {
            val eventDto = testTool.createAndInsertEventDto()
            repeat(3) {
                val quizDto = testTool.createAndInsertQuizDto(eventDto)
                quizDtoList.add(quizDto)
                repeat(4) {
                    val choiceDto = testTool.createChoiceDto(quizDto)
                    choiceDtoList.add(choiceDto)
                    updateChoiceDtoList.add(
                        choiceDto.copy().apply {
                            body = testTool.uuid
                            isDeleted = testTool.trueOrFalse
                            quizId = quizDtoList.random().identifier.id
                        },
                    )
                }
            }
        }

        return Pair(choiceDtoList, updateChoiceDtoList)
    }
}
