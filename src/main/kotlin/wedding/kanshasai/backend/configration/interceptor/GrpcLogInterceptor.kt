package wedding.kanshasai.backend.configration.interceptor

import com.google.protobuf.GeneratedMessageV3
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.grpc.*
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import org.slf4j.MDC
import java.util.*

private val logger = KotlinLogging.logger {}
const val REQUEST_ID_KEY = "request-id"

class GrpcLogInterceptor: ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val requestId = "grpc_${UUID.randomUUID()}"
        MDC.put(REQUEST_ID_KEY, requestId)

        logger.at(Level.DEBUG) {
            this.message = "Request opened"
            this.payload = mapOf(
                "grpc" to mapOf(
                    "serviceName" to "${call.methodDescriptor.serviceName}",
                    "methodName" to "${call.methodDescriptor.bareMethodName}",
                    "headers" to headers.keys().associateWith { key -> headers.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)) }
                )
            )
        }

        val listener = Contexts.interceptCall(Context.current(), object : SimpleForwardingServerCall<ReqT, RespT>(call) {
            override fun request(numMessages: Int) {
                MDC.put(REQUEST_ID_KEY, requestId)
                super.request(numMessages)
            }

            override fun sendMessage(message: RespT) {
                MDC.put(REQUEST_ID_KEY, requestId)
                super.sendMessage(message)
            }

            override fun close(status: Status?, trailers: Metadata?) {
                MDC.put(REQUEST_ID_KEY, requestId)
                super.close(status, trailers)
                logger.at(Level.DEBUG) {
                    this.message = "Request closed"
                    this.payload = mapOf(
                        "grpc" to mapOf(
                            "serviceName" to "${call.methodDescriptor.serviceName}",
                            "methodName" to "${call.methodDescriptor.bareMethodName}",
                            "headers" to headers.keys().associateWith { key -> headers.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)) }
                        )
                    )
                }
                MDC.clear()
            }
        }, headers, next)

        return object : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            override fun onMessage(message: ReqT) {
                val parsedMessage = message as GeneratedMessageV3

                val level = if (call.methodDescriptor.fullMethodName.startsWith("wedding.kanshasai")) Level.INFO else Level.DEBUG
                logger.at(level) {
                    this.message = "Request message received"
                    this.payload = mapOf(
                        "grpc" to mapOf(
                            "serviceName" to "${call.methodDescriptor.serviceName}",
                            "methodName" to "${call.methodDescriptor.bareMethodName}",
                            "headers" to headers.keys().associateWith { key -> headers.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)) },
                            "messages" to parsedMessage.allFields.mapKeys { field -> field.key.name }
                        )
                    )
                }
                super.onMessage(message)
            }
        }
    }
}
