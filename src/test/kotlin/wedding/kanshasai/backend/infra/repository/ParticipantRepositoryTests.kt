package wedding.kanshasai.backend.infra.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.any
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.exception.DatabaseException
import wedding.kanshasai.backend.infra.mysql.dto.EventDto
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mysql.dto.QuizDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantMapper
import wedding.kanshasai.backend.infra.mysql.repository.ParticipantRepository
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("ParticipantRepository")
class ParticipantRepositoryTests {

    @SpyBean
    private lateinit var participantMapper: ParticipantMapper

    @Autowired
    private lateinit var participantRepository: ParticipantRepository

    @Autowired
    private lateinit var mapperTestTool: MapperTestTool

    companion object {
        const val INVALID_PARTICIPANT_ID = "0AAAAAAAAAAAAAAAAAAAAAAAAA"
        const val INVALID_SESSION_ID = "0AAAAAAAAAAAAAAAAAAAAAAAAA"
    }

    private lateinit var eventDto: EventDto
    private lateinit var sessionDto: SessionDto
    private lateinit var participantEmptySessionDto: SessionDto
    private lateinit var participantDto: ParticipantDto
    private lateinit var participantDtoList: List<ParticipantDto>

    @BeforeAll
    fun beforeAll() {
        eventDto = mapperTestTool.createEventDto(UlidId.new())
        sessionDto = mapperTestTool.createSessionDto(eventDto, UlidId.new())
        participantEmptySessionDto = mapperTestTool.createSessionDto(eventDto, UlidId.new())
        participantDto = mapperTestTool.createParticipantDto(sessionDto, UlidId.new(), UlidId.new())
        participantDtoList = (1..10).map { mapperTestTool.createParticipantDto(sessionDto, UlidId.new()) }
    }

    @BeforeEach
    fun beforeEach() {
        mapperTestTool.truncateAll()
        mapperTestTool.eventMapper.insert(eventDto)
        mapperTestTool.sessionMapper.insert(sessionDto)
        mapperTestTool.sessionMapper.insert(participantEmptySessionDto)
        mapperTestTool.participantMapper.insert(participantDto)
        participantDtoList.forEach {
            mapperTestTool.participantMapper.insert(it)
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findById_parameters")
    @DisplayName("findById")
    fun <T : Throwable> findById_test(testCaseName: String, id: UlidId, expect: Participant?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                participantRepository.findById(id).getOrThrow()
            }
            return
        }
        val event = participantRepository.findById(id).getOrThrow()
        if (expect == null) {
            assertNull(event)
            return
        }
        assertEquals(expect.id, event.id)
        assertEquals(expect.sessionId, event.sessionId)
        assertEquals(expect.name, event.name)
        assertEquals(expect.imageId, event.imageId)
        assertEquals(expect.isDeleted, event.isDeleted)
    }

    fun findById_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しい参加者IDを渡すと参加者が返される",
                UlidId.of(participantDto.participantIdentifier.id),
                Participant.of(participantDto),
                null,
            ),
            arguments(
                "異常系 存在しない参加者IDを渡すとDatabaseExceptionが投げられる",
                UlidId.of(INVALID_PARTICIPANT_ID),
                null,
                DatabaseException::class.java,
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listBySession_parameters")
    @DisplayName("listBySession()")
    fun <T : Throwable> listBySessionId_test(testCaseName: String, session: Session, expect: List<ParticipantDto>?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                participantRepository.listBySession(session).getOrThrow()
            }
            return
        }
        val participantList = participantRepository.listBySession(session).getOrThrow()
        expect?.map { Participant.of(it) }?.forEach { expectParticipant ->
            val participant = participantList.find { participant -> participant.id == expectParticipant.id }
            assertNotNull(participant)
            participant?.let {
                assertEquals(expectParticipant.id, participant.id)
                assertEquals(expectParticipant.sessionId, participant.sessionId)
                assertEquals(expectParticipant.name, participant.name)
                assertEquals(expectParticipant.imageId, participant.imageId)
                assertEquals(expectParticipant.isDeleted, participant.isDeleted)
            }
        }
    }

    fun listBySession_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいセッションIDを渡すと参加者の配列が返される",
                Session.of(sessionDto),
                participantDtoList,
                null,
            ),
            arguments(
                "正常系 クイズが0件なセッションIDを渡すと0件の参加者の配列が返される",
                Session.of(participantEmptySessionDto),
                listOf<QuizDto>(),
                null,
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("createParticipant_parameters")
    @DisplayName("createParticipant()")
    fun <T : Throwable> createParticipant_test(
        testCaseName: String,
        session: Session,
        participantName: String,
        imageId: UlidId?,
        type: ParticipantType,
        throwable: Class<T>?,
        dbFailFlag: Boolean,
    ) {
        if (dbFailFlag) {
            doReturn(0).`when`(participantMapper).insert(any(ParticipantDto::class.java))
        }
        if (throwable != null) {
            assertThrows(throwable) {
                participantRepository.createParticipant(session, participantName, imageId, type).getOrThrow()
            }
            return
        }
        val createdParticipant = participantRepository.createParticipant(session, participantName, imageId, type).getOrThrow()
        assertEquals(participantName, createdParticipant.name)
        assertEquals(session.id, createdParticipant.sessionId)
        assertEquals(imageId, createdParticipant.imageId)
        assertEquals(type, createdParticipant.type)
        assertFalse(createdParticipant.isDeleted)

        val foundParticipant = participantRepository.findById(createdParticipant.id).getOrThrow()
        assertEquals(createdParticipant.id, foundParticipant.id)
        assertEquals(createdParticipant.name, foundParticipant.name)
        assertEquals(createdParticipant.sessionId, foundParticipant.sessionId)
        assertEquals(createdParticipant.imageId, foundParticipant.imageId)
        assertEquals(createdParticipant.type, foundParticipant.type)
        assertEquals(createdParticipant.isDeleted, foundParticipant.isDeleted)
    }

    fun createParticipant_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいname、imageIdを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                "participant_name",
                UlidId.new(),
                ParticipantType.values.random(),
                null,
                false,
            ),
            arguments(
                "正常系 正しいnameのみを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                "participant_name",
                null,
                ParticipantType.values.random(),
                null,
                false,
            ),
            arguments(
                "正常系 既に存在するnameとユニークなimageIdを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                participantDto.name,
                UlidId.new(),
                ParticipantType.values.random(),
                null,
                false,
            ),
            arguments(
                "正常系 既に存在するnameと既に存在するimageIdを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                participantDto.name,
                participantDto.imageId?.let(UlidId::of),
                ParticipantType.values.random(),
                null,
                false,
            ),
            arguments(
                "異常系 nameに空文字を渡すとInvalidArgumentExceptionが投げられる",
                Session.of(sessionDto),
                "",
                null,
                ParticipantType.values.random(),
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 nameに空文字かつDBに存在しないイベントを渡すとInvalidArgumentExceptionが投げられる",
                Session.of(SessionDto(UlidId.new().toStandardIdentifier(), eventDto.eventIdentifier.id)),
                "",
                null,
                ParticipantType.values.random(),
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 DBへの挿入に失敗するとDatabaseExceptionが投げられる",
                Session.of(sessionDto),
                "participant_name",
                null,
                ParticipantType.values.random(),
                DatabaseException::class.java,
                true,
            ),
        )
    }
}
