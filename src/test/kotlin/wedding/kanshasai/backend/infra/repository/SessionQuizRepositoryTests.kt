package wedding.kanshasai.backend.infra.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.mysql.dto.EventDto
import wedding.kanshasai.backend.infra.mysql.dto.QuizDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.SessionQuizMapper
import wedding.kanshasai.backend.infra.mysql.repository.SessionQuizRepository
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("SessionQuizRepository")
class SessionQuizRepositoryTests {

    @SpyBean
    private lateinit var sessionQuizMapper: SessionQuizMapper

    @Autowired
    private lateinit var sessionQuizRepository: SessionQuizRepository

    @Autowired
    private lateinit var mapperTestTool: MapperTestTool

    private lateinit var eventDto: EventDto
    private lateinit var sessionDto: SessionDto
    private lateinit var quizDtoList: List<QuizDto>
    private lateinit var insertedQuizDtoList: List<QuizDto>
    private lateinit var sessionQuizDtoList: List<SessionQuizDto>

    private lateinit var eventDto2: EventDto
    private lateinit var sessionDto2: SessionDto

    @BeforeAll
    fun beforeAll() {
        eventDto = mapperTestTool.createEventDto(UlidId.new())
        sessionDto = mapperTestTool.createSessionDto(eventDto, UlidId.new())
        quizDtoList = (1..10).map { mapperTestTool.createQuizDto(eventDto, UlidId.new()) }
        insertedQuizDtoList = (1..10).map { mapperTestTool.createQuizDto(eventDto, UlidId.new()) }
        sessionQuizDtoList = insertedQuizDtoList.map {
            mapperTestTool.createSessionQuizDto(sessionDto, it)
        }

        eventDto2 = mapperTestTool.createEventDto(UlidId.new())
        sessionDto2 = mapperTestTool.createSessionDto(eventDto2, UlidId.new())
    }

    @BeforeEach
    fun beforeEach() {
        mapperTestTool.truncateAll()
        mapperTestTool.eventMapper.insert(eventDto)
        mapperTestTool.sessionMapper.insert(sessionDto)
        quizDtoList.forEach(mapperTestTool.quizMapper::insert)
        insertedQuizDtoList.forEach(mapperTestTool.quizMapper::insert)
        sessionQuizDtoList.forEach(mapperTestTool.sessionQuizMapper::insert)

        mapperTestTool.eventMapper.insert(eventDto2)
        mapperTestTool.sessionMapper.insert(sessionDto2)
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("find_parameters")
    @DisplayName("find()")
    fun <T : Throwable> find_test(testCaseName: String, session: Session, quiz: Quiz, expect: SessionQuiz?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                sessionQuizRepository.find(session, quiz).getOrThrow()
            }
            return
        }
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrow()
        if (expect == null) {
            assertNull(sessionQuiz)
            return
        }
        assertEquals(expect.sessionId, sessionQuiz.sessionId)
        assertEquals(expect.quizId, sessionQuiz.quizId)
        assertEquals(expect.isCompleted, sessionQuiz.isCompleted)
        assertEquals(expect.startedAt, sessionQuiz.startedAt)
        assertEquals(expect.isDeleted, sessionQuiz.isDeleted)
    }

    fun find_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいセッションIDとクイズIDを渡すとイベントが返される",
                Session.of(sessionDto),
                Quiz.of(insertedQuizDtoList.first()),
                SessionQuiz.of(sessionQuizDtoList.first()),
                null,
            ),
            arguments(
                "異常系 存在しないセッションIDと存在するクイズIDを渡すとNotFoundExceptionが投げられる",
                Session.of(sessionDto.copy(sessionIdentifier = UlidId.new().toStandardIdentifier())),
                Quiz.of(insertedQuizDtoList.first()),
                null,
                NotFoundException::class.java,
            ),
            arguments(
                "異常系 存在するセッションIDと存在しないクイズIDを渡すとNotFoundExceptionが投げられる",
                Session.of(sessionDto),
                Quiz.of(insertedQuizDtoList.first().copy(quizIdentifier = UlidId.new().toStandardIdentifier())),
                null,
                NotFoundException::class.java,
            ),
            arguments(
                "異常系 存在しないセッションIDと存在しないクイズIDを渡すとNotFoundExceptionが投げられる",
                Session.of(sessionDto.copy(sessionIdentifier = UlidId.new().toStandardIdentifier())),
                Quiz.of(insertedQuizDtoList.first().copy(quizIdentifier = UlidId.new().toStandardIdentifier())),
                null,
                NotFoundException::class.java,
            ),
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("insertQuizList_parameters")
    @DisplayName("insertQuizList")
    fun <T : Throwable> insertQuizList_test(
        testCaseName: String,
        session: Session,
        quizList: List<Quiz>,
        expect: List<SessionQuiz>?,
        throwable: Class<T>?,
        dbFailFlag: Boolean,
    ) {
        if (dbFailFlag) {
            Mockito.doReturn(0).`when`(sessionQuizMapper).insertAll(ArgumentMatchers.anyList())
        }
        if (throwable != null) {
            assertThrows(throwable) {
                sessionQuizRepository.insertQuizList(session, quizList).getOrThrow()
            }
            return
        }
        val count = mapperTestTool.sessionQuizMapper.select().size
        sessionQuizRepository.insertQuizList(session, quizList).getOrThrow()

        expect?.let {
            assertEquals(it.size, mapperTestTool.sessionQuizMapper.select().size - count)
            it.forEach { expectSessionQuiz ->
                val sessionQuizDto = mapperTestTool.sessionQuizMapper.findById(
                    SessionQuizIdentifier(expectSessionQuiz.sessionId.toByteArray(), expectSessionQuiz.quizId.toByteArray()),
                )
                assertNotNull(sessionQuizDto)
                assertEquals(
                    expectSessionQuiz.sessionId,
                    sessionQuizDto?.sessionQuizIdentifier?.sessionId?.let(UlidId::of),
                )
                assertEquals(
                    expectSessionQuiz.quizId,
                    sessionQuizDto?.sessionQuizIdentifier?.quizId?.let(UlidId::of),
                )
                assertEquals(expectSessionQuiz.isCompleted, sessionQuizDto?.isCompleted)
                assertEquals(expectSessionQuiz.startedAt, sessionQuizDto?.startedAt)
                assertEquals(expectSessionQuiz.isDeleted, sessionQuizDto?.isDeleted)
            }
        }
    }

    fun insertQuizList_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいセッションとクイズ配列を渡すと正常に終了する",
                Session.of(sessionDto),
                quizDtoList.map { Quiz.of(it) },
                quizDtoList.map {
                    SessionQuiz.of(
                        SessionQuizDto(
                            SessionQuizIdentifier(sessionDto.sessionIdentifier.id, it.identifier.id),
                        ),
                    )
                },
                null,
                false,
            ),
            arguments(
                "正常系 件数が0なクイズ配列を渡すと正常に終了する",
                Session.of(sessionDto),
                listOf<QuizDto>(),
                listOf<QuizDto>(),
                null,
                false,
            ),
            arguments(
                "異常系 存在しないセッションIDを渡すとNotFoundExceptionが投げられる",
                Session.of(SessionDto(UlidId.new().toStandardIdentifier(), UlidId.new().toByteArray())),
                quizDtoList.map { Quiz.of(it) },
                null,
                NotFoundException::class.java,
                false,
            ),
            arguments(
                "異常系 クイズ配列にDBに存在しないクイズが含まれているとDataIntegrityViolationExceptionが投げられる",
                Session.of(sessionDto),
                quizDtoList.map { Quiz.of(it.copy(quizIdentifier = UlidId.new().toStandardIdentifier())) },
                null,
                DataIntegrityViolationException::class.java,
                false,
            ),
            arguments(
                "異常系 クイズ配列に既に存在するセッションクイズが含まれているとDuplicateKeyExceptionが投げられる",
                Session.of(sessionDto),
                insertedQuizDtoList.map { Quiz.of(it) },
                null,
                DuplicateKeyException::class.java,
                false,
            ),
            arguments(
                "異常系 クイズ配列にセッションと異なるイベントのクイズが含まれているとInvalidArgumentExceptionが投げられる",
                Session.of(sessionDto2),
                quizDtoList.map { Quiz.of(it) },
                null,
                InvalidArgumentException::class.java,
                false,
            ),
            arguments(
                "異常系 DBのinsertに失敗するとDatabaseExceptionが投げられる",
                Session.of(sessionDto),
                quizDtoList.map { Quiz.of(it) },
                quizDtoList.map {
                    SessionQuiz.of(
                        SessionQuizDto(
                            SessionQuizIdentifier(sessionDto.sessionIdentifier.id, it.identifier.id),
                        ),
                    )
                },
                DatabaseException::class.java,
                true,
            ),
        )
    }
}
