package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.Param

interface MapperCRUDBase<T> {
    fun findById(
        @Param("id") id: ByteArray,
    ): T?

    fun insert(
        @Param("entity") entity: T,
    ): Int

    fun select(
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<T>

    fun deleteById(
        @Param("id") id: ByteArray,
    ): Boolean
}