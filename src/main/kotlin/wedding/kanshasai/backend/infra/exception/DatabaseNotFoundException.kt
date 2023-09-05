package wedding.kanshasai.backend.infra.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.value.UlidId
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
        fun retrieve(
            table: Table,
            columnName1: String,
            id1: UlidId,
            columnName2: String,
            id2: UlidId,
            cause: Throwable?,
        ): DatabaseException {
            return DatabaseNotFoundException("Failed to retrieve from '${table.tableName}' ($columnName1=$id1, $columnName2=$id2).", cause)
        }
    }
}
