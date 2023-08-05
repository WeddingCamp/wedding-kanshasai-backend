package wedding.kanshasai.backend.configration.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Metadata
import io.grpc.Status
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.slf4j.MDC
import wedding.kanshasai.backend.configration.interceptor.REQUEST_ID_KEY
import java.lang.RuntimeException

private val logger = KotlinLogging.logger {}

@GrpcAdvice
class GrpcExceptionHandler {
    @GrpcExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedException(e: NotImplementedError) = createStatus(Status.UNIMPLEMENTED, e)

    @GrpcExceptionHandler(Exception::class)
    fun handleException(e: Exception) = createStatus(Status.INTERNAL, e)

    fun createStatus(status: Status, e: Throwable): RuntimeException {
        logger.atError {
            cause = e.cause
            message = e.message
        }

        val metadata = Metadata()
        metadata.put(Metadata.Key.of(REQUEST_ID_KEY, Metadata.ASCII_STRING_MARSHALLER), MDC.get(REQUEST_ID_KEY))

        return status
            .withCause(e)
            .withDescription(e.message)
            .asRuntimeException(metadata)
    }
}
