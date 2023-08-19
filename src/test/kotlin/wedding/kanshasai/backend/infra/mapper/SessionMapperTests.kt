package wedding.kanshasai.backend.infra.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.entity.Event
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.repository.EventRepository

@WeddingKanshasaiSpringBootTest
class SessionMapperTests {

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var sessionMapper: SessionMapper

    val sessionTestStubList = mutableListOf<SessionTestStub>()

    data class SessionTestStub(
        val event: Event,
        val sessionDtoList: List<SessionDto>,
        val sessionUlidIdList: List<UlidId>,
    )

    @BeforeAll
    fun beforeAll() {
        for (i in 1..3) {
            sessionTestStubList.add(
                SessionTestStub(
                    eventRepository.createEvent("Event $i").getOrThrow(),
                    mutableListOf(),
                    (1..i).map { UlidId.new() },
                ),
            )
        }
    }

    @Test
    @Order(1)
    fun listSessions_shouldReturnArray_listSizeEqualsZero() {
        assertEquals(0, sessionMapper.select().size)
    }

    @Test
    @Order(2)
    fun createSession_shouldSucceed() {
        sessionTestStubList.forEach { stub ->
            stub.sessionUlidIdList.forEach { sessionId ->
                val result = sessionMapper.insert(
                    SessionDto(
                        sessionId.toByteArray(),
                        stub.event.id.toByteArray(),
                        stub.event.name,
                    ),
                )
                assertEquals(1, result)
            }
        }
    }

    @Test
    @Order(3)
    fun findById_shouldReturnSessionWithEvent() {
        sessionTestStubList.forEach { stub ->
            stub.sessionUlidIdList.forEach { sessionId ->
                val session = sessionMapper.findById(sessionId.toByteArray())
                assertNotNull(session!!)
                assertEquals(sessionId, UlidId.of(session.id))
                assertEquals(stub.event.id, UlidId.of(session.eventId))
                assertEquals(stub.event.name, session.name)

                assertNotNull(session.event)
                assertEquals(stub.event.id, UlidId.of(session.event!!.id))
                assertEquals(stub.event.name, session.event!!.name)
                assertEquals(stub.event.isDeleted, session.event!!.isDeleted)
            }
        }
    }

    @Test
    @Order(4)
    fun listSessions_shouldReturnArray_listSizeEqualsSessionSize() {
        assertEquals(sessionTestStubList.flatMap { it.sessionUlidIdList }.size, sessionMapper.select().size)
    }

    @Test
    @Order(5)
    fun deleteSession_shouldSucceed() {
        sessionTestStubList[0].sessionUlidIdList.forEach {
            assert(sessionMapper.deleteById(it.toByteArray()))
        }
    }

    @Test
    @Order(6)
    fun listSessions_shouldReturnArray_listSizeEqualsSessionSize_whenRecordDeleted() {
        assertEquals(sessionTestStubList.flatMap { it.sessionUlidIdList }.size - 1, sessionMapper.select().size)
    }

    @Test
    @Order(6)
    fun listSessions_shouldReturnArray_listSizeEqualsSessionSizePlusOne_whenRecordDeletedAndIncludeDeleted() {
        assertEquals(sessionTestStubList.flatMap { it.sessionUlidIdList }.size, sessionMapper.select(true).size)
    }
}
