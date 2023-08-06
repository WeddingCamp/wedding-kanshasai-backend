package wedding.kanshasai.backend

import de.huxhorn.sulky.ulid.ULID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.repository.EventMapper

@WeddingKanshasaiSpringBootTest
class EventMapperTests {

    @Autowired
    lateinit var eventMapper: EventMapper

    @Test
    @Order(1)
    fun listEvents_shouldReturnArray_listSizeEqualsZero() {
        assertEquals(eventMapper.listEvents().size, 0)
    }

    @Test
    @Order(2)
    fun createEvent_shouldSucceed() = assertDoesNotThrow {
        eventMapper.createEvent(ULID().nextValue().toBytes(), "Test Event 1")
        eventMapper.createEvent(ULID().nextValue().toBytes(), "Test Event 2")
    }

    @Test
    @Order(3)
    fun listEvents_shouldReturnArray_listSizeEqualsTwo() {
        assertEquals(eventMapper.listEvents().size, 2)
    }

    @Test
    @Order(3)
    fun findEvent_shouldReturnEvent() {
        val list = eventMapper.listEvents()
        val event = eventMapper.findById(list[0].id)

        assertNotNull(event)
        assertEquals(ULID.fromBytes(list[0].id), ULID.fromBytes(event?.id))
        assertEquals(list[0].name, event?.name)
        assertEquals(list[0].createdAt, event?.createdAt)
        assertEquals(list[0].updatedAt, event?.updatedAt)
    }

    @Test
    @Order(4)
    fun deleteEvent_shouldSucceed() = assertDoesNotThrow {
        val list = eventMapper.listEvents()
        assert(eventMapper.deleteById(list[0].id))
    }

    @Test
    @Order(5)
    fun listEvents_shouldReturnArray_listSizeEqualsOne_whenEventDeleted() {
        assertEquals(eventMapper.listEvents().size, 1)
    }

    @Test
    @Order(5)
    fun listEvents_shouldReturnArray_listSizeEqualsTwo_whenEventDeletedAndIncludeDeleted() {
        assertEquals(eventMapper.listEvents(true).size, 2)
    }
}
