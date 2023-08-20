package wedding.kanshasai.backend.domain.exception

import wedding.kanshasai.backend.domain.value.UlidId

class DatabaseException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun failedToRetrieve(name: String, id: UlidId, cause: Throwable?): DatabaseException {
            return DatabaseException("Failed to retrieve $name(id=$id).", cause)
        }
        fun failedToInsert(name: String): DatabaseException {
            return DatabaseException("Failed to insert $name.")
        }
    }
}
