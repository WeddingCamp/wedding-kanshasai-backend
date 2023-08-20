package wedding.kanshasai.backend.domain.exception

import wedding.kanshasai.backend.domain.constant.Table
import wedding.kanshasai.backend.domain.value.UlidId

class NotFoundException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun record(table: Table, id: UlidId, cause: Throwable?): NotFoundException {
            return NotFoundException("Not found ${table.tableName}(id=$id).", cause)
        }
    }
}
