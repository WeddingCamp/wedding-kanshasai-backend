package wedding.kanshasai.backend.infra

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.domain.value.QuizResultType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mapper.*
import wedding.kanshasai.backend.infra.mysql.dto.*
import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.*
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
        return eventMapper.findById(eventDto.eventIdentifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createSessionDto(eventDto: EventDto, sessionId: UlidId = UlidId.new()): SessionDto {
        return SessionDto(
            sessionId.toStandardIdentifier(),
            eventDto.eventIdentifier.id,
            "Session_$sessionId",
            SessionState.values.random().toNumber(),
            maybeNull(UlidId.new().toByteArray()),
            listOf(0, 1, 2).random(),
            QuizResultType.values.random().toNumber(),
            1, // TODO: ResultState関連のテストを書く
            1,
            100,
            trueOrFalse,
        )
    }

    fun createAndInsertSessionDto(eventDto: EventDto, sessionId: UlidId = UlidId.new()): SessionDto {
        val sessionDto = createSessionDto(eventDto, sessionId)
        sessionMapper.insert(sessionDto)
        return sessionMapper.findById(sessionDto.sessionIdentifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createParticipantDto(sessionDto: SessionDto, participantId: UlidId = UlidId.new(), imageId: UlidId? = null): ParticipantDto {
        return ParticipantDto(
            participantId.toStandardIdentifier(),
            sessionDto.sessionIdentifier.id,
            "Participant_$participantId",
            "Participant_Ruby_$participantId",
            imageId?.toByteArray() ?: maybeNull(UlidId.new().toByteArray()),
            ParticipantType.values.random().toNumber(),
        )
    }

    fun createAndInsertParticipantDto(sessionDto: SessionDto, participantId: UlidId = UlidId.new()): ParticipantDto {
        val participantDto = createParticipantDto(sessionDto, participantId)
        participantMapper.insert(participantDto)
        return participantMapper.findById(participantDto.participantIdentifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createQuizDto(eventDto: EventDto, quizId: UlidId = UlidId.new()): QuizDto {
        return QuizDto(
            quizId.toStandardIdentifier(),
            eventDto.eventIdentifier.id,
            "Quiz_body_$quizId",
            "Quiz_answer_$quizId",
            listOf(1, 2).random(),
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
            SessionQuizIdentifier(sessionDto.sessionIdentifier.id, quizDto.identifier.id),
            trueOrFalse,
            maybeNull(Timestamp(System.currentTimeMillis())),
        )
    }

    fun createAndInsertSessionQuizDto(sessionDto: SessionDto, quizDto: QuizDto): SessionQuizDto {
        val sessionQuizDto = createSessionQuizDto(sessionDto, quizDto)
        sessionQuizMapper.insert(sessionQuizDto)
        return sessionQuizMapper.findById(sessionQuizDto.sessionQuizIdentifier) ?: throw RuntimeException("Failed to create dto.")
    }

    fun createParticipantAnswerDto(participantDto: ParticipantDto, sessionQuizDto: SessionQuizDto): ParticipantAnswerDto {
        return ParticipantAnswerDto(
            ParticipantAnswerIdentifier(participantDto.participantIdentifier.id, sessionQuizDto.sessionQuizIdentifier),
            "{\"answer\": \"${(1..4).random()}\"}",
            (Math.random() * 10).toFloat(),
        )
    }

    fun createAndInsertParticipantAnswerDto(participantDto: ParticipantDto, sessionQuizDto: SessionQuizDto): ParticipantAnswerDto {
        val participantAnswerDto = createParticipantAnswerDto(participantDto, sessionQuizDto)
        participantAnswerMapper.insert(participantAnswerDto)
        return participantAnswerMapper.findById(participantAnswerDto.participantAnswerIdentifier)
            ?: throw RuntimeException("Failed to create dto.")
    }
}
