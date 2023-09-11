package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantAnswerMapper

@WeddingKanshasaiSpringBootTest
class ParticipantAnswerMapperTests : MapperCRUDTest<ParticipantAnswerMapper, ParticipantAnswerIdentifier, ParticipantAnswerDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<ParticipantAnswerDto>, List<ParticipantAnswerDto>> {
        val participantAnswerDtoList = mutableListOf<ParticipantAnswerDto>()
        val updateParticipantAnswerDtoList = mutableListOf<ParticipantAnswerDto>()

        repeat(3) {
            val eventDto = testTool.createAndInsertEventDto()
            val quizDtoList = (1..10).map {
                testTool.createAndInsertQuizDto(eventDto)
            }
            val sessionDtoList = (0..2).map {
                testTool.createAndInsertSessionDto(eventDto)
            }
            val sessionQuizDtoList = sessionDtoList.map { session ->
                quizDtoList.map { quiz ->
                    testTool.createAndInsertSessionQuizDto(session, quiz)
                }
            }.flatten()
            val participantDtoList = sessionDtoList.map { session ->
                (0..(10..50).random()).map {
                    testTool.createAndInsertParticipantDto(session)
                }
            }.flatten()

            participantDtoList.forEach { participant ->
                sessionQuizDtoList.forEach { sessionQuiz ->
                    val participantAnswerDto = testTool.createParticipantAnswerDto(participant, sessionQuiz)
                    participantAnswerDtoList.add(participantAnswerDto)
                    updateParticipantAnswerDtoList.add(
                        // TODO: 依存している関係も入れ替えたい
                        participantAnswerDto.copy().apply {
                            answer = testTool.uuid
                            time = (Math.random() * 10).toFloat()
                            isDeleted = testTool.trueOrFalse
                        },
                    )
                }
            }
        }
        return Pair(participantAnswerDtoList, updateParticipantAnswerDtoList)
    }
}
