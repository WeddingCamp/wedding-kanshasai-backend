package wedding.kanshasai.backend.domain.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.value.UlidId

class NotFoundException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun record(table: Table, id: UlidId, cause: Throwable?): NotFoundException {
            return NotFoundException("Not found ${table.tableName}(id=$id).", cause)
        }
        fun record(table: Table, columnName: String, id: UlidId, cause: Throwable?): NotFoundException {
            return NotFoundException("Not found ${table.tableName}($columnName=$id).", cause)
        }
        fun record(table: Table, columnName1: String, id1: UlidId, columnName2: String, id2: UlidId, cause: Throwable?): NotFoundException {
            return NotFoundException("Not found ${table.tableName}($columnName1=$id1, $columnName2=$id2).", cause)
        }
    }
}
