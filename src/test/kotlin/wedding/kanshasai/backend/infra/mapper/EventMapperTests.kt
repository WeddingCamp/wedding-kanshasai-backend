package wedding.kanshasai.backend.infra.mapper

import de.huxhorn.sulky.ulid.ULID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.dto.EventDto

@WeddingKanshasaiSpringBootTest
class EventMapperTests {

    @Autowired
    lateinit var eventMapper: EventMapper

    @Test
    @Order(1)
    fun listEvents_shouldReturnArray_listSizeEqualsZero() {
        assertEquals(0, eventMapper.select().size)
    }

    @Test
    @Order(2)
    fun createEvent_shouldSucceed() {
        assertDoesNotThrow {
            eventMapper.insert(EventDto(UlidId.new().toByteArray(), "Test Event 1"))
            eventMapper.insert(EventDto(UlidId.new().toByteArray(), "Test Event 2"))
        }
    }

    @Test
    @Order(3)
    fun listEvents_shouldReturnArray_listSizeEqualsTwo() {
        assertEquals(2, eventMapper.select().size)
    }

    @Test
    @Order(3)
    fun findEvent_shouldReturnEvent() {
        val list = eventMapper.select()
        val event = eventMapper.findById(list[0].id)

        assertNotNull(event!!)
        assertEquals(ULID.fromBytes(list[0].id), ULID.fromBytes(event.id))
        assertEquals(list[0].name, event.name)
    }

    @Test
    @Order(4)
    fun deleteEvent_shouldSucceed() {
        assertDoesNotThrow {
            val list = eventMapper.select()
            assert(eventMapper.deleteById(list[0].id))
        }
    }

    @Test
    @Order(5)
    fun listEvents_shouldReturnArray_listSizeEqualsOne_whenEventDeleted() {
        assertEquals(1, eventMapper.select().size)
    }

    @Test
    @Order(5)
    fun listEvents_shouldReturnArray_listSizeEqualsTwo_whenEventDeletedAndIncludeDeleted() {
        assertEquals(2, eventMapper.select(true).size)
    }
}
