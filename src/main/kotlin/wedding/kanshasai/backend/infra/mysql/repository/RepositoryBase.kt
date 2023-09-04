package wedding.kanshasai.backend.infra.mysql.repository

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.infra.exception.DatabaseException
import wedding.kanshasai.backend.infra.mysql.dto.IdentifiableDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.DtoIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.MapperCRUDBase

abstract class RepositoryBase {
    abstract val table: Table

    fun <ID : DtoIdentifier, DTO : IdentifiableDto<ID>> findById(mapper: MapperCRUDBase<ID, DTO>, id: ID, t: Table = table): DTO {
        val result = mapper.findById(id)
        if (result == null) throw DatabaseException.retrieve(t, id, null)
        return result
    }

    fun <ID : DtoIdentifier, DTO : IdentifiableDto<ID>> insert(mapper: MapperCRUDBase<ID, DTO>, dto: DTO, t: Table = table) {
        val result = mapper.insert(dto)
        if (result != 1) throw DatabaseException.insert(t, 1, result, null)
    }

    fun <ID : DtoIdentifier, DTO : IdentifiableDto<ID>> insertAll(mapper: MapperCRUDBase<ID, DTO>, dtoList: List<DTO>, t: Table = table) {
        val result = mapper.insertAll(dtoList)
        if (result != dtoList.size) throw DatabaseException.insert(t, dtoList.size, result, null)
    }

    fun <ID : DtoIdentifier, DTO : IdentifiableDto<ID>> update(mapper: MapperCRUDBase<ID, DTO>, dto: DTO, t: Table = table) {
        val isSucceed = mapper.update(dto)
        if (isSucceed) throw DatabaseException.update(t, dto.identifier, null)
    }
}
