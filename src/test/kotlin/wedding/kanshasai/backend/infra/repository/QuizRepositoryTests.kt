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
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.mysql.dto.EventDto
import wedding.kanshasai.backend.infra.mysql.dto.QuizDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.repository.QuizRepository
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
    @MethodSource("listByEvent_parameters")
    @DisplayName("listByEvent()")
    fun <T : Throwable> listByEvent_test(testCaseName: String, event: Event, expect: List<QuizDto>?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                quizRepository.listByEvent(event).getOrThrow()
            }
            return
        }

        val quizList = quizRepository.listByEvent(event).getOrThrow()
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

    fun listByEvent_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいイベントIDを渡すとクイズの配列が返される",
                Event.of(eventDto),
                quizDtoList,
                null,
            ),
            arguments(
                "正常系 クイズが0件なイベントIDを渡すと0件のクイズの配列が返される",
                Event.of(quizEmptyEventDto),
                listOf<QuizDto>(),
                null,
            ),
        )
    }
}
