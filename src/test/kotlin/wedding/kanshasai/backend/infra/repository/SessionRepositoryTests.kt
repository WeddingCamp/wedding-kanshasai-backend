package wedding.kanshasai.backend.infra.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.exception.NotFoundException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.EventDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import java.util.stream.Stream

@WeddingKanshasaiSpringBootTest
@DisplayName("SessionRepository")
class SessionRepositoryTests {

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
    fun <T : Throwable> findById_test(name: String, id: UlidId, expect: Session?, throwable: Class<T>?) {
        if (throwable == null) {
            Assertions.assertDoesNotThrow {
                val session = sessionRepository.findById(id).getOrThrow()
                if (expect != null) {
                    assertEquals(expect.id, session.id)
                    assertEquals(expect.name, session.name)
                    assertEquals(expect.stateId, session.stateId)
                    assertEquals(expect.coverScreenId, session.coverScreenId)
                    assertEquals(expect.currentQuizId, session.currentQuizId)
                    assertEquals(expect.isDeleted, session.isDeleted)
                }
            }
        } else {
            Assertions.assertThrows(throwable) {
                sessionRepository.findById(id).getOrThrow()
            }
        }
    }

    fun findById_parameters(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "正常系 正しいIDを渡すとイベントが返される",
                UlidId.of(sessionDto.identifier.id).getOrThrow(),
                Session.of(sessionDto),
                null,
            ),
            arguments(
                "異常系 存在しないIDを渡すとNotFoundExceptionが投げられる",
                UlidId.of(INVALID_SESSION_ID).getOrThrow(),
                null,
                NotFoundException::class.java,
            ),
        )
    }
}
