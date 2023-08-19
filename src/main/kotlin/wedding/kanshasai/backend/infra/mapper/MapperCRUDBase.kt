package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.Param

interface MapperCRUDBase<T> {
    // CREATE
    fun insert(
        @Param("entity") entity: T,
    ): Int

    // READ
    fun select(
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<T>

    fun findById(
        @Param("id") id: ByteArray,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): T?

    // UPDATE
    fun update(
        @Param("entity") entity: T,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): Boolean

    // DELETE
    fun deleteById(
        @Param("id") id: ByteArray,
    ): Boolean
}
