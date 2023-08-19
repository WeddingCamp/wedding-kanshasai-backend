package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.ParticipantAnswerDto
import wedding.kanshasai.backend.infra.dto.identifier.ParticipantAnswerIdentifier

@WeddingKanshasaiSpringBootTest
class ParticipantAnswerMapperTests : MapperCRUDTest<ParticipantAnswerMapper, ParticipantAnswerIdentifier, ParticipantAnswerDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList() = (0..2).map {
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
            (0..(10..100).random()).map {
                testTool.createAndInsertParticipantDto(session)
            }
        }.flatten()

        participantDtoList.map { participant ->
            sessionQuizDtoList.map { sessionQuiz ->
                testTool.createParticipantAnswerDto(participant, sessionQuiz)
            }
        }.flatten()
    }.flatten()
}
