package wedding.kanshasai.backend.infra.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.infra.mysql.dto.identifier.DtoIdentifier

open class DatabaseException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
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
