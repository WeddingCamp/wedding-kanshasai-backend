package wedding.kanshasai.backend.infra

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.*
import wedding.kanshasai.backend.infra.mapper.*

@Component
class MapperTestTool {

    @Autowired
    lateinit var eventMapper: EventMapper

    @Autowired
    lateinit var sessionMapper: SessionMapper

    @Autowired
    lateinit var participantMapper: ParticipantMapper

    @Autowired
    lateinit var quizMapper: QuizMapper

    @Autowired
    lateinit var choiceMapper: ChoiceMapper

    fun createEventDto(): EventDto {
        val eventId = UlidId.new()
        return EventDto(
            eventId.toByteArray(),
            "Event_$eventId",
        )
    }

    fun createAndInsertEventDto(): EventDto {
        val eventDto = createEventDto()
        eventMapper.insert(eventDto)
        return eventDto
    }

    fun createSessionDto(eventDto: EventDto): SessionDto {
        val sessionId = UlidId.new()
        return SessionDto(
            sessionId.toByteArray(),
            eventDto.id,
            "Session_$sessionId",
            (0..100).random(),
            (0..100).random(),
            null,
            event = eventDto,
        )
    }

    fun createAndInsertSessionDto(eventDto: EventDto): SessionDto {
        val sessionDto = createSessionDto(eventDto)
        sessionMapper.insert(sessionDto)
        return sessionDto
    }

    fun createParticipantDto(sessionDto: SessionDto): ParticipantDto {
        val participantId = UlidId.new()
        val imageId = UlidId.new()
        return ParticipantDto(
            participantId.toByteArray(),
            sessionDto.id,
            "Participant_$participantId",
            imageId.toByteArray(),
            session = sessionDto,
        )
    }

    fun createAndInsertParticipantDto(sessionDto: SessionDto): ParticipantDto {
        val participantDto = createParticipantDto(sessionDto)
        participantMapper.insert(participantDto)
        return participantDto
    }

    fun createQuizDto(eventDto: EventDto): QuizDto {
        val quizId = UlidId.new()
        return QuizDto(
            quizId.toByteArray(),
            eventDto.id,
            "Quiz_body_$quizId",
            "Quiz_answer_$quizId",
            (0..100).random(),
            event = eventDto,
        )
    }

    fun createAndInsertQuizDto(eventDto: EventDto): QuizDto {
        val quizDto = createQuizDto(eventDto)
        quizMapper.insert(quizDto)
        return quizDto
    }

    fun createChoiceDto(quizDto: QuizDto): ChoiceDto {
        val choiceId = UlidId.new()
        return ChoiceDto(
            choiceId.toByteArray(),
            quizDto.id,
            "Choice_$choiceId",
            quiz = quizDto,
        )
    }

    fun createAndInsertChoiceDto(quizDto: QuizDto): ChoiceDto {
        val choiceDto = createChoiceDto(quizDto)
        choiceMapper.insert(choiceDto)
        return choiceDto
    }
}
