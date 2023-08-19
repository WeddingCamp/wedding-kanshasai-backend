package wedding.kanshasai.backend.infra.repository

import de.huxhorn.sulky.ulid.ULID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest

@WeddingKanshasaiSpringBootTest
class EventRepositoryTests {

    @Autowired
    lateinit var eventRepository: EventRepository

    @Test
    @Order(1)
    fun listEvents_shouldReturnArray_listSizeEqualsZero() {
        val eventList = eventRepository.listEvents().getOrThrow()
        assertEquals(0, eventList.size)
    }

    @Test
    @Order(2)
    fun createEvent_shouldSucceedAndReturnValidUlidId() {
        val test1Name = "Test Event 1"
        val test2Name = "Test Event 2"
        val event1 = eventRepository.createEvent(test1Name).getOrThrow()
        val event2 = eventRepository.createEvent(test2Name).getOrThrow()
        assertDoesNotThrow {
            ULID.parseULID(event1.id.toString())
            ULID.parseULID(event2.id.toString())
        }
        assertNotEquals(event1.id, event2.id)
        assertEquals(test1Name, event1.name)
        assertEquals(test2Name, event2.name)
    }

    @Test
    @Order(3)
    fun listEvents_shouldReturnArray_listSizeEqualsTwo() {
        val eventList = eventRepository.listEvents().getOrThrow()
        assertEquals(2, eventList.size)
    }

    @Test
    @Order(3)
    fun findEvent_shouldReturnEvent() {
        val list = eventRepository.listEvents().getOrThrow()
        val event = eventRepository.findById(list[0].id).getOrThrow()

        assertNotNull(event)
        assertEquals(list[0].id, event.id)
        assertEquals(list[0].name, event.name)
    }

    @Test
    @Order(4)
    fun deleteEvent_shouldSucceed() {
        val list = eventRepository.listEvents().getOrThrow()
        val currentSize = list.size
        assertDoesNotThrow {
            eventRepository.deleteEvent(list[0].id).getOrThrow()
        }
        val list2 = eventRepository.listEvents().getOrThrow()
        assertEquals(currentSize - 1, list2.size)
    }
}
