package wedding.kanshasai.backend.infra.repository

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.any
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.mapper.SessionMapper
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("SessionRepository")
class SessionRepositoryTests {

    @SpyBean
    private lateinit var sessionMapper: SessionMapper

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    @Autowired
    private lateinit var mapperTestTool: MapperTestTool

    companion object {
        const val INVALID_SESSION_ID = "0AAAAAAAAAAAAAAAAAAAAAAAAA"
    }

    private lateinit var eventDto: EventDto
    private lateinit var sessionDto: SessionDto

    @BeforeAll
    fun beforeAll() {
        eventDto = mapperTestTool.createEventDto(UlidId.new())
        sessionDto = mapperTestTool.createSessionDto(eventDto, UlidId.new())
    }

    @BeforeEach
    fun beforeEach() {
        mapperTestTool.truncateAll()
        mapperTestTool.eventMapper.insert(eventDto)
        mapperTestTool.sessionMapper.insert(sessionDto)
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findById_parameters")
    @DisplayName("findById()")
    fun <T : Throwable> findById_test(testCaseName: String, id: UlidId, expect: Session?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                sessionRepository.findById(id).getOrThrow()
            }
            return
        }
        val session = sessionRepository.findById(id).getOrThrow()
        if (expect == null) {
            assertNull(session)
            return
        }
        assertEquals(expect.id, session.id)
        assertEquals(expect.name, session.name)
        assertEquals(expect.stateId, session.stateId)
        assertEquals(expect.coverScreenId, session.coverScreenId)
        assertEquals(expect.currentQuizId, session.currentQuizId)
        assertEquals(expect.isDeleted, session.isDeleted)
    }

    fun findById_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいセッションIDを渡すとイベントが返される",
                UlidId.of(sessionDto.identifier.id),
                Session.of(sessionDto),
                null,
            ),
            arguments(
                "異常系 存在しないセッションIDを渡すとNotFoundExceptionが投げられる",
                UlidId.of(INVALID_SESSION_ID),
                null,
                NotFoundException::class.java,
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("createSession_parameters")
    @DisplayName("createSession()")
    fun <T : Throwable> createSession_test(
        testCaseName: String,
        event: Event,
        sessionName: String,
        throwable: Class<T>?,
        dbFailFlag: Boolean,
    ) {
        if (dbFailFlag) {
            Mockito.doReturn(0).`when`(sessionMapper).insert(any(SessionDto::class.java))
        }
        if (throwable != null) {
            assertThrows(throwable) {
                sessionRepository.createSession(event, sessionName).getOrThrow()
            }
            return
        }
        val createdSession = sessionRepository.createSession(event, sessionName).getOrThrow()
        assertEquals(sessionName, createdSession.name)
        assertEquals(1, createdSession.stateId)
        assertNull(createdSession.coverScreenId)
        assertNull(createdSession.currentQuizId)
        assertFalse(createdSession.isDeleted)

        val foundSession = sessionRepository.findById(createdSession.id).getOrThrow()
        assertEquals(createdSession.id, foundSession.id)
        assertEquals(createdSession.name, foundSession.name)
        assertEquals(createdSession.stateId, foundSession.stateId)
        assertEquals(createdSession.coverScreenId, foundSession.coverScreenId)
        assertEquals(createdSession.currentQuizId, foundSession.currentQuizId)
        assertEquals(createdSession.isDeleted, foundSession.isDeleted)
    }

    fun createSession_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいnameを渡すとセッションレコードが挿入され、セッションが返される",
                Event.of(eventDto),
                "session_name",
                null,
                false,
            ),
            arguments(
                "正常系 既に存在するセッション名を渡すとセッションレコードが挿入され、セッションが返される",
                Event.of(eventDto),
                sessionDto.name,
                null,
                false,
            ),
            arguments(
                "異常系 DBに存在しないイベントを渡すとNotFoundExceptionが投げられる",
                Event.of(EventDto(UlidId.new().toStandardIdentifier(), "event_name")),
                "session_name",
                NotFoundException::class.java,
                false,
            ),
            arguments(
                "異常系 nameに空文字を渡すとInvalidArgumentExceptionが投げられる",
                Event.of(eventDto),
                "",
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 nameに空文字かつDBに存在しないイベントを渡すとInvalidArgumentExceptionが投げられる",
                Event.of(EventDto(UlidId.new().toStandardIdentifier(), "event_name")),
                "",
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 DBのinsertに失敗するとDatabaseExceptionが投げられる",
                Event.of(eventDto),
                "session_name",
                DatabaseException::class.java,
                true,
            ),
        )
    }
}
