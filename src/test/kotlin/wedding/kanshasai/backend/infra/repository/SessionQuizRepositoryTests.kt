package wedding.kanshasai.backend.infra.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.dto.SessionQuizDto
import wedding.kanshasai.backend.infra.dto.identifier.SessionQuizIdentifier
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("SessionQuizRepository")
class SessionQuizRepositoryTests {

    @Autowired
    private lateinit var sessionQuizRepository: SessionQuizRepository

    @Autowired
    private lateinit var mapperTestTool: MapperTestTool

    companion object {
//        const val INVALID_EVENT_ID = "0AAAAAAAAAAAAAAAAAAAAAAAAA"
    }

    private lateinit var eventDto: EventDto
    private lateinit var sessionDto: SessionDto
    private lateinit var quizDtoList: List<QuizDto>

    @BeforeAll
    fun beforeAll() {
        eventDto = mapperTestTool.createEventDto(UlidId.new())
        sessionDto = mapperTestTool.createSessionDto(eventDto, UlidId.new())
        quizDtoList = (1..10).map { mapperTestTool.createQuizDto(eventDto, UlidId.new()) }
    }

    @BeforeEach
    fun beforeEach() {
        mapperTestTool.truncateAll()
        mapperTestTool.eventMapper.insert(eventDto)
        mapperTestTool.sessionMapper.insert(sessionDto)
        quizDtoList.forEach {
            mapperTestTool.quizMapper.insert(it)
        }
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
    ) {
        if (throwable != null) {
            assertThrows(throwable) {
                sessionQuizRepository.insertQuizList(session, quizList).getOrThrow()
            }
            return
        }
        sessionQuizRepository.insertQuizList(session, quizList).getOrThrow()

        expect?.forEach { expectSessionQuiz ->
            val sessionQuizDto = mapperTestTool.sessionQuizMapper.findById(
                SessionQuizIdentifier(expectSessionQuiz.sessionId.toByteArray(), expectSessionQuiz.quizId.toByteArray()),
            )
            assertNotNull(sessionQuizDto)
            assertEquals(
                expectSessionQuiz.sessionId,
                sessionQuizDto?.identifier?.sessionId?.let { UlidId.of(it).getOrThrow() },
            )
            assertEquals(
                expectSessionQuiz.quizId,
                sessionQuizDto?.identifier?.quizId?.let { UlidId.of(it).getOrThrow() },
            )
            assertEquals(expectSessionQuiz.isCompleted, sessionQuizDto?.isCompleted)
            assertEquals(expectSessionQuiz.startedAt, sessionQuizDto?.startedAt)
            assertEquals(expectSessionQuiz.isDeleted, sessionQuizDto?.isDeleted)
        }
    }

    fun insertQuizList_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいイベントIDを渡すとイベントが返される",
                Session.of(sessionDto),
                quizDtoList.map { Quiz.of(it) },
                quizDtoList.map {
                    SessionQuiz.of(
                        SessionQuizDto(
                            SessionQuizIdentifier(sessionDto.identifier.id, it.identifier.id),
                        ),
                    )
                },
                null,
            ),
            // 既に存在するセッションクイズ全件
            // 既に存在するセッションクイズを含む

//            arguments(
//                "異常系 存在しないイベントIDを渡すとNotFoundExceptionが投げられる",
//                UlidId.of(INVALID_EVENT_ID).getOrThrow(),
//                null,
//                NotFoundException::class.java,
//            ),
        )
    }
}
