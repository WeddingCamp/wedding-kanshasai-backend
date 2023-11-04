package wedding.kanshasai.backend.infra.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.infra.mysql.dto.identifier.DtoIdentifier

class DatabaseNotFoundException(message: String? = null, cause: Throwable? = null) : DatabaseException(message, cause) {
    companion object {
        fun retrieve(table: Table, cause: Throwable?): DatabaseNotFoundException {
            return DatabaseNotFoundException("Failed to retrieve from '${table.tableName}'.", cause)
        }
        fun retrieve(table: Table, id: DtoIdentifier, cause: Throwable?): DatabaseNotFoundException {
            return retrieve(table, "id", id, cause)
        }
        fun retrieve(table: Table, columnName: String, id: DtoIdentifier, cause: Throwable?): DatabaseNotFoundException {
            return DatabaseNotFoundException("Failed to retrieve from '${table.tableName}' ($columnName=$id).", cause)
        }
    }
}
