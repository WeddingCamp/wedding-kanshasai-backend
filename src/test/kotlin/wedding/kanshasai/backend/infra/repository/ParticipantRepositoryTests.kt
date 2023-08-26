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
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.any
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.ParticipantDto
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.mapper.ParticipantMapper
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
                UlidId.of(participantDto.identifier.id).getOrThrow(),
                Participant.of(participantDto),
                null,
            ),
            arguments(
                "異常系 存在しない参加者IDを渡すとNotFoundExceptionが投げられる",
                UlidId.of(INVALID_PARTICIPANT_ID).getOrThrow(),
                null,
                NotFoundException::class.java,
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listBySessionId_parameters")
    @DisplayName("listBySessionId()")
    fun <T : Throwable> listBySessionId_test(testCaseName: String, id: UlidId, expect: List<ParticipantDto>?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                participantRepository.listBySessionId(id).getOrThrow()
            }
            return
        }
        val participantList = participantRepository.listBySessionId(id).getOrThrow()
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

    fun listBySessionId_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいセッションIDを渡すと参加者の配列が返される",
                UlidId.of(sessionDto.identifier.id).getOrThrow(),
                participantDtoList,
                null,
            ),
            arguments(
                "正常系 クイズが0件なセッションIDを渡すと0件の参加者の配列が返される",
                UlidId.of(participantEmptySessionDto.identifier.id).getOrThrow(),
                listOf<QuizDto>(),
                null,
            ),
            arguments(
                "異常系 存在しないセッションIDを渡すとNotFoundExceptionが投げられる",
                UlidId.of(INVALID_SESSION_ID).getOrThrow(),
                null,
                NotFoundException::class.java,
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
        throwable: Class<T>?,
        dbFailFlag: Boolean,
    ) {
        if (dbFailFlag) {
            doReturn(0).`when`(participantMapper).insert(any(ParticipantDto::class.java))
        }
        if (throwable != null) {
            assertThrows(throwable) {
                participantRepository.createParticipant(session, participantName, imageId).getOrThrow()
            }
            return
        }
        val createdParticipant = participantRepository.createParticipant(session, participantName, imageId).getOrThrow()
        assertEquals(participantName, createdParticipant.name)
        assertEquals(session.id, createdParticipant.sessionId)
        assertEquals(imageId, createdParticipant.imageId)
        assertFalse(createdParticipant.isDeleted)

        val foundSession = participantRepository.findById(createdParticipant.id).getOrThrow()
        assertEquals(createdParticipant.id, foundSession.id)
        assertEquals(createdParticipant.name, foundSession.name)
        assertEquals(createdParticipant.sessionId, foundSession.sessionId)
        assertEquals(createdParticipant.imageId, foundSession.imageId)
        assertEquals(createdParticipant.isDeleted, foundSession.isDeleted)
    }

    fun createParticipant_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいname、imageIdを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                "participant_name",
                UlidId.new(),
                null,
                false,
            ),
            arguments(
                "正常系 正しいnameのみを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                "participant_name",
                null,
                null,
                false,
            ),
            arguments(
                "正常系 既に存在するnameとユニークなimageIdを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                participantDto.name,
                UlidId.new(),
                null,
                false,
            ),
            arguments(
                "正常系 既に存在するnameと既に存在するimageIdを渡すと参加者レコードが挿入され、参加者が返される",
                Session.of(sessionDto),
                participantDto.name,
                participantDto.imageId?.let { UlidId.of(it).getOrThrow() },
                null,
                false,
            ),
            arguments(
                "異常系 DBに存在しないセッションを渡すとNotFoundExceptionが投げられる",
                Session.of(SessionDto(UlidId.new().toStandardIdentifier(), eventDto.identifier.id)),
                "participant_name",
                null,
                NotFoundException::class.java,
                false,
            ),
            arguments(
                "異常系 nameに空文字を渡すとInvalidArgumentExceptionが投げられる",
                Session.of(sessionDto),
                "",
                null,
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 nameに空文字かつDBに存在しないイベントを渡すとInvalidArgumentExceptionが投げられる",
                Session.of(SessionDto(UlidId.new().toStandardIdentifier(), eventDto.identifier.id)),
                "",
                null,
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 DBへの挿入に失敗するとDatabaseExceptionが投げられる",
                Session.of(sessionDto),
                "participant_name",
                null,
                DatabaseException::class.java,
                true,
            ),
        )
    }
}
