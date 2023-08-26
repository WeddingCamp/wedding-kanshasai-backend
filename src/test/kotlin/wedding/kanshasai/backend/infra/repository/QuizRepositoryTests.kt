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
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.QuizDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("QuizRepository")
class QuizRepositoryTests {

    @Autowired
    private lateinit var quizRepository: QuizRepository

    @Autowired
    private lateinit var mapperTestTool: MapperTestTool

    companion object {
        const val INVALID_EVENT_ID = "0AAAAAAAAAAAAAAAAAAAAAAAAA"
    }

    private lateinit var eventDto: EventDto
    private lateinit var quizEmptyEventDto: EventDto
    private lateinit var sessionDto: SessionDto
    private lateinit var quizDtoList: List<QuizDto>

    @BeforeAll
    fun beforeAll() {
        eventDto = mapperTestTool.createEventDto(UlidId.new())
        quizEmptyEventDto = mapperTestTool.createEventDto(UlidId.new())
        sessionDto = mapperTestTool.createSessionDto(eventDto, UlidId.new())
        quizDtoList = (1..10).map { mapperTestTool.createQuizDto(eventDto, UlidId.new()) }
    }

    @BeforeEach
    fun beforeEach() {
        mapperTestTool.truncateAll()
        mapperTestTool.eventMapper.insert(eventDto)
        mapperTestTool.eventMapper.insert(quizEmptyEventDto)
        mapperTestTool.sessionMapper.insert(sessionDto)
        quizDtoList.forEach {
            mapperTestTool.quizMapper.insert(it)
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listByEventId_parameters")
    @DisplayName("listByEventId()")
    fun <T : Throwable> listByEventId_test(testCaseName: String, id: UlidId, expect: List<QuizDto>?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                quizRepository.listByEventId(id).getOrThrow()
            }
            return
        }

        val quizList = quizRepository.listByEventId(id).getOrThrow()
        expect?.map { Quiz.of(it) }?.forEach { expectQuiz ->
            val quiz = quizList.find { quiz -> quiz.id == expectQuiz.id }
            assertNotNull(quiz)
            quiz?.let {
                assertEquals(expectQuiz.id, quiz.id)
                assertEquals(expectQuiz.eventId, quiz.eventId)
                assertEquals(expectQuiz.body, quiz.body)
                assertEquals(expectQuiz.correctAnswer, quiz.correctAnswer)
                assertEquals(expectQuiz.type, quiz.type)
                assertEquals(expectQuiz.isDeleted, quiz.isDeleted)
            }
        }
    }

    fun listByEventId_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいイベントIDを渡すとクイズの配列が返される",
                UlidId.of(eventDto.identifier.id),
                quizDtoList,
                null,
            ),
            arguments(
                "正常系 クイズが0件なイベントIDを渡すと0件のクイズの配列が返される",
                UlidId.of(quizEmptyEventDto.identifier.id),
                listOf<QuizDto>(),
                null,
            ),
            arguments(
                "異常系 存在しないイベントIDを渡すとNotFoundExceptionが投げられる",
                UlidId.of(INVALID_EVENT_ID),
                null,
                NotFoundException::class.java,
            ),
        )
    }
}
