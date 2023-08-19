package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.Param

interface MapperCRUDBase<IDENTIFIER, DTO> {
    // CREATE
    fun insert(
        @Param("entity") entity: DTO,
    ): Int

    // READ
    fun select(
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<DTO>

    fun findById(
        @Param("identifier") identifier: IDENTIFIER,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): DTO?

    // UPDATE
    fun update(
        @Param("entity") entity: DTO,
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): Boolean

    // DELETE
    fun deleteById(
        @Param("identifier") identifier: IDENTIFIER,
    ): Boolean
}
