package wedding.kanshasai.backend.infra.mapper

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.infra.dto.IdentifiableDto

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class MapperCRUDTest<MAPPER : MapperCRUDBase<DTO>, DTO : IdentifiableDto> {

    @Autowired
    lateinit var mapper: MAPPER

    private val dtoList = mutableListOf<DTO>()

    abstract fun stubDtoList(): List<DTO>

    @BeforeAll
    fun beforeAll() {
        dtoList.addAll(stubDtoList())
    }

    @Test
    @Order(10)
    fun select_shouldReturnArray_listSizeEqualsZero() {
        assertEquals(0, mapper.select().size)
    }

    @Test
    @Order(20)
    fun insert_shouldSucceed() {
        dtoList.forEach {
            assertEquals(1, mapper.insert(it))
        }
    }

    @Test
    @Order(30)
    fun select_shouldReturnArray_listSizeEqualsDtoListSize() {
        assertEquals(dtoList.size, mapper.select().size)
    }

    @Test
    @Order(30)
    fun select_shouldReturnArray_listItemIsCorrect() {
        val list = mapper.select()
        dtoList.forEach { dto ->
            val item = list.find { it.id.contentEquals(dto.id) }
            assert(dto.strictEquals(item))
        }
    }

    @Test
    @Order(40)
    fun findById_shouldReturnDto_dtoIsCorrect() {
        dtoList.forEach {
            assert(it.strictEquals(mapper.findById(it.id)))
        }
    }

    @Test
    @Order(50)
    fun delete_shouldSucceed() {
        dtoList.forEach {
            assert(mapper.deleteById(it.id))
        }
        assertEquals(0, mapper.select().size)
    }

    @Test
    @Order(60)
    fun select_shouldReturnArray_listSizeEqualsDtoListSize_whenIncludeDeleted() {
        assertEquals(dtoList.size, mapper.select(true).size)
    }
}
