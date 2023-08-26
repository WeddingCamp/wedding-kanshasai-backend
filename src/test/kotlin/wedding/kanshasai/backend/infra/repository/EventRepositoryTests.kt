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
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("EventRepository")
class EventRepositoryTests {

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var mapperTestTool: MapperTestTool

    companion object {
        const val INVALID_EVENT_ID = "0AAAAAAAAAAAAAAAAAAAAAAAAA"
    }

    private lateinit var eventDto: EventDto

    @BeforeAll
    fun beforeAll() {
        eventDto = mapperTestTool.createEventDto(UlidId.new())
    }

    @BeforeEach
    fun beforeEach() {
        mapperTestTool.truncateAll()
        mapperTestTool.eventMapper.insert(eventDto)
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findById_parameters")
    @DisplayName("findById")
    fun <T : Throwable> findById_test(testCaseName: String, id: UlidId, expect: Event?, throwable: Class<T>?) {
        if (throwable != null) {
            assertThrows(throwable) {
                eventRepository.findById(id).getOrThrow()
            }
            return
        }
        val event = eventRepository.findById(id).getOrThrow()
        if (expect == null) {
            assertNull(event)
            return
        }
        assertEquals(expect.id, event.id)
        assertEquals(expect.name, event.name)
        assertEquals(expect.isDeleted, event.isDeleted)
    }

    fun findById_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいイベントIDを渡すとイベントが返される",
                UlidId.of(eventDto.identifier.id).getOrThrow(),
                Event.of(eventDto),
                null,
            ),
            arguments(
                "異常系 存在しないイベントIDを渡すとNotFoundExceptionが投げられる",
                UlidId.of(INVALID_EVENT_ID).getOrThrow(),
                null,
                NotFoundException::class.java,
            ),
        )
    }
}
