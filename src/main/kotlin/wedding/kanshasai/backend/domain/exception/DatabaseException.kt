package wedding.kanshasai.backend.domain.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.value.UlidId

class DatabaseException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun failedToRetrieve(table: Table, id: UlidId, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to retrieve ${table.tableName}(id=$id).", cause)
        }
        fun failedToRetrieve(table: Table, columnName: String, id: UlidId, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to retrieve ${table.tableName}($columnName=$id).", cause)
        }
        fun failedToInsert(table: Table, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to insert ${table.tableName}.", cause)
        }
        fun incorrectNumberOfInsert(table: Table, expect: Int, actual: Int, cause: Throwable?): DatabaseException {
            return DatabaseException(
                "Number of records inserted differs from the number of records that attempted to be inserted. " +
                    "Table: ${table.tableName}, Expect: $expect, Actual: $actual",
                cause,
            )
        }
    }
}
