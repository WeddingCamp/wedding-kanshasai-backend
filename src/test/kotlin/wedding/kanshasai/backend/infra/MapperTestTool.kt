package wedding.kanshasai.backend.infra

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.*
import wedding.kanshasai.backend.infra.dto.identifier.ParticipantAnswerIdentifier
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mapper.*
import java.lang.RuntimeException
import java.sql.Timestamp
import java.util.*

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

    @Autowired
    lateinit var sessionQuizMapper: SessionQuizMapper

    @Autowired
    lateinit var participantAnswerMapper: ParticipantAnswerMapper

    @Autowired
    lateinit var truncateMapper: TruncateMapper

    val uuid: String get() = UUID.randomUUID().toString()
    val trueOrFalse: Boolean get() = listOf(true, false).random()
    fun <T> maybeNull(v: T): T? = listOf(v, null).random()

    fun truncateAll() {
        truncateMapper.truncate()
    }

    fun createEventDto(eventId: UlidId = UlidId.new()): EventDto {
        return EventDto(
            eventId.toStandardIdentifier(),
            "Event_$eventId",
        )
    }

    fun createAndInsertEventDto(eventId: UlidId = UlidId.new()): EventDto {
        val eventDto = createEventDto(eventId)
        eventMapper.insert(eventDto)
        return eventMapper.findById(eventDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createSessionDto(eventDto: EventDto, sessionId: UlidId = UlidId.new()): SessionDto {
        return SessionDto(
            sessionId.toStandardIdentifier(),
            eventDto.identifier.id,
            "Session_$sessionId",
            (0..100).random(),
            maybeNull((0..100).random()),
            maybeNull(UlidId.new().toByteArray()),
        )
    }

    fun createAndInsertSessionDto(eventDto: EventDto, sessionId: UlidId = UlidId.new()): SessionDto {
        val sessionDto = createSessionDto(eventDto, sessionId)
        sessionMapper.insert(sessionDto)
        return sessionMapper.findById(sessionDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createParticipantDto(sessionDto: SessionDto, participantId: UlidId = UlidId.new()): ParticipantDto {
        return ParticipantDto(
            participantId.toStandardIdentifier(),
            sessionDto.identifier.id,
            "Participant_$participantId",
            maybeNull(UlidId.new().toByteArray()),
        )
    }

    fun createAndInsertParticipantDto(sessionDto: SessionDto, participantId: UlidId = UlidId.new()): ParticipantDto {
        val participantDto = createParticipantDto(sessionDto, participantId)
        participantMapper.insert(participantDto)
        return participantMapper.findById(participantDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createQuizDto(eventDto: EventDto, quizId: UlidId = UlidId.new()): QuizDto {
        return QuizDto(
            quizId.toStandardIdentifier(),
            eventDto.identifier.id,
            "Quiz_body_$quizId",
            "Quiz_answer_$quizId",
            (0..100).random(),
        )
    }

    fun createAndInsertQuizDto(eventDto: EventDto, quizId: UlidId = UlidId.new()): QuizDto {
        val quizDto = createQuizDto(eventDto, quizId)
        quizMapper.insert(quizDto)
        return quizMapper.findById(quizDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createChoiceDto(quizDto: QuizDto, choiceId: UlidId = UlidId.new()): ChoiceDto {
        return ChoiceDto(
            choiceId.toStandardIdentifier(),
            quizDto.identifier.id,
            "Choice_$choiceId",
        )
    }

    fun createAndInsertChoiceDto(quizDto: QuizDto, choiceId: UlidId = UlidId.new()): ChoiceDto {
        val choiceDto = createChoiceDto(quizDto, choiceId)
        choiceMapper.insert(choiceDto)
        return choiceMapper.findById(choiceDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createSessionQuizDto(sessionDto: SessionDto, quizDto: QuizDto): SessionQuizDto {
        return SessionQuizDto(
            SessionQuizIdentifier(sessionDto.identifier.id, quizDto.identifier.id),
            trueOrFalse,
            maybeNull(Timestamp(System.currentTimeMillis())),
        )
    }

    fun createAndInsertSessionQuizDto(sessionDto: SessionDto, quizDto: QuizDto): SessionQuizDto {
        val sessionQuizDto = createSessionQuizDto(sessionDto, quizDto)
        sessionQuizMapper.insert(sessionQuizDto)
        return sessionQuizMapper.findById(sessionQuizDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createParticipantAnswerDto(participantDto: ParticipantDto, sessionQuizDto: SessionQuizDto): ParticipantAnswerDto {
        return ParticipantAnswerDto(
            ParticipantAnswerIdentifier(participantDto.identifier.id, sessionQuizDto.identifier),
            "{\"answer\": \"${(1..4).random()}\"}",
            maybeNull((Math.random() * 10).toFloat()),
        )
    }

    fun createAndInsertParticipantAnswerDto(participantDto: ParticipantDto, sessionQuizDto: SessionQuizDto): ParticipantAnswerDto {
        val participantAnswerDto = createParticipantAnswerDto(participantDto, sessionQuizDto)
        participantAnswerMapper.insert(participantAnswerDto)
        return participantAnswerMapper.findById(participantAnswerDto.identifier) ?: throw RuntimeException("Failed to create dto.")
    }
}
