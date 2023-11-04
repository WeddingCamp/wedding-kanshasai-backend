package wedding.kanshasai.backend.configration.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Metadata
import io.grpc.Status
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.slf4j.MDC
import wedding.kanshasai.backend.configration.interceptor.REQUEST_ID_KEY
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.service.exception.FailedOperationException
import wedding.kanshasai.backend.service.exception.NotFoundException
import java.lang.RuntimeException

private val logger = KotlinLogging.logger {}

@GrpcAdvice
class GrpcExceptionHandler {
    @GrpcExceptionHandler(NotImplementedError::class)
    fun handleException(e: NotImplementedError) = createStatus(Status.UNIMPLEMENTED, e)

    @GrpcExceptionHandler(NotFoundException::class)
    fun handleException(e: NotFoundException) = createStatus(Status.NOT_FOUND, e)

    @GrpcExceptionHandler(FailedOperationException::class)
    fun handleException(e: FailedOperationException) = createStatus(Status.INTERNAL, e)

    @GrpcExceptionHandler(InvalidArgumentException::class)
    fun handleException(e: InvalidArgumentException) = createStatus(Status.INVALID_ARGUMENT, e)

    @GrpcExceptionHandler(InvalidStateException::class)
    fun handleException(e: InvalidStateException) = createStatus(Status.FAILED_PRECONDITION, e)

    @GrpcExceptionHandler(Exception::class)
    fun handleException(e: Exception) = createStatus(Status.INTERNAL, e)

    fun createStatus(status: Status, e: Throwable): RuntimeException {
        logger.atError {
            message = e.message
            cause = e
        }

        val metadata = Metadata()
        metadata.put(Metadata.Key.of(REQUEST_ID_KEY, Metadata.ASCII_STRING_MARSHALLER), MDC.get(REQUEST_ID_KEY))
        logger.info { metadata.toString() }

        return status
            .withCause(e)
            .withDescription(e.message)
            .asRuntimeException(metadata)
    }
}
