package wedding.kanshasai.backend.infra.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.dto.identifier.DtoIdentifier

class DatabaseException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun retrieve(table: Table, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to retrieve from '${table.tableName}'.", cause)
        }
        fun retrieve(table: Table, id: DtoIdentifier, cause: Throwable?): DatabaseException {
            return retrieve(table, "id", id, cause)
        }
        fun retrieve(table: Table, columnName: String, id: DtoIdentifier, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to retrieve from '${table.tableName}' ($columnName=$id).", cause)
        }
        fun retrieve(
            table: Table,
            columnName1: String,
            id1: UlidId,
            columnName2: String,
            id2: UlidId,
            cause: Throwable?,
        ): DatabaseException {
            return DatabaseException("Failed to retrieve from '${table.tableName}' ($columnName1=$id1, $columnName2=$id2).", cause)
        }
        fun insert(table: Table, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to insert into '${table.tableName}'.", cause)
        }
        fun insert(table: Table, expect: Int, actual: Int, cause: Throwable?): DatabaseException {
            return DatabaseException(
                "Number of records inserted differs from the number of records that attempted to be inserted. " +
                    "Table: ${table.tableName}, Expect: $expect, Actual: $actual",
                cause,
            )
        }
        fun update(table: Table, id: DtoIdentifier, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to update ${table.tableName}(id=$id).", cause)
        }
    }
}
