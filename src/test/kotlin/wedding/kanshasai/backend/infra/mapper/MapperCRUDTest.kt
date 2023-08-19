package wedding.kanshasai.backend.infra.mapper

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.infra.dto.IdentifiableDto
import wedding.kanshasai.backend.infra.dto.identifier.DtoIdentifier

@WeddingKanshasaiSpringBootTest
abstract class MapperCRUDTest<MAPPER : MapperCRUDBase<IDENTIFIER, DTO>, IDENTIFIER : DtoIdentifier, DTO : IdentifiableDto<IDENTIFIER>> {

    @Autowired
    lateinit var mapper: MAPPER

    private val dtoList = mutableListOf<DTO>()
    private val updateDtoList = mutableListOf<DTO>()

    abstract fun stubDtoList(): Pair<List<DTO>, List<DTO>>

    @BeforeAll
    fun beforeAll() {
        val result = stubDtoList()
        dtoList.addAll(result.first)
        updateDtoList.addAll(result.second)
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
            val item = list.find { it.identifier == dto.identifier }
            assert(dto.strictEquals(item))
        }
    }

    @Test
    @Order(40)
    fun findById_shouldReturnDto_dtoIsCorrect() {
        dtoList.forEach {
            assert(it.strictEquals(mapper.findById(it.identifier, true)))
        }
    }

    @Test
    @Order(50)
    fun update_shouldSucceed() {
        updateDtoList.forEach { dto ->
            assert(mapper.update(dto))
        }
    }

    @Test
    @Order(60)
    fun select_shouldReturnArray_listItemIsCorrect_whenUpdated() {
        val list = mapper.select(true)
        updateDtoList.forEach { dto ->
            val item = list.find { it.identifier == dto.identifier }
            assert(dto.strictEquals(item))
        }
    }

    @Test
    @Order(70)
    fun delete_shouldSucceed_whenEntityIsNotDeleted() {
        updateDtoList.filter { !it.isDeleted }.forEach {
            assert(mapper.deleteById(it.identifier))
        }
    }

    @Test
    @Order(80)
    fun delete_shouldFail_whenEntityIsDeleted() {
        dtoList.forEach {
            assertFalse(mapper.deleteById(it.identifier))
        }
    }

    @Test
    @Order(90)
    fun select_shouldReturnArray_listSizeEqualsZero_whenAllEntityDeleted() {
        assertEquals(0, mapper.select().size)
    }

    @Test
    @Order(90)
    fun select_shouldReturnArray_listSizeEqualsDtoListSize_whenIncludeDeleted() {
        assertEquals(dtoList.size, mapper.select(true).size)
    }
}
