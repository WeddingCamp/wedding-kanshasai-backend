package wedding.kanshasai.backend.service.exception

class NotFoundException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    companion object {
        fun resource(cause: Throwable?): NotFoundException {
            return NotFoundException("Requested resource not found.", cause)
        }
    }
}
