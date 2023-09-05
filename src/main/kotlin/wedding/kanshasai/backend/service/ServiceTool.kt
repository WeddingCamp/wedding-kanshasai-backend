package wedding.kanshasai.backend.service

import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.infra.exception.DatabaseNotFoundException
import wedding.kanshasai.backend.service.exception.FailedOperationException
import wedding.kanshasai.backend.service.exception.NotFoundException

fun <T> Result<T>.getOrThrowService(): T {
    return try {
        this.getOrThrow()
    } catch (e: InvalidStateException) {
        throw e
    } catch (e: DatabaseNotFoundException) {
        throw NotFoundException.resource(e)
    } catch (e: Exception) {
        throw FailedOperationException(
            "Failed to ${e.stackTrace.first().className.split(".").last()}#${e.stackTrace.first().methodName}()",
            e,
        )
    }
}
