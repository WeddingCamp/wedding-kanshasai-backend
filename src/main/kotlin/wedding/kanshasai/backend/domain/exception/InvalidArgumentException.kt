package wedding.kanshasai.backend.domain.exception

class InvalidArgumentException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun requiredField(fieldName: String, cause: Throwable? = null): InvalidArgumentException {
            return InvalidArgumentException("'$fieldName' is required.", cause)
        }
        fun empty(fieldName: String, cause: Throwable? = null): InvalidArgumentException {
            return InvalidArgumentException("'$fieldName' is empty.", cause)
        }
    }
}
