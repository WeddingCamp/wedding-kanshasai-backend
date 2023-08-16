package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase

@GrpcService
class ScreenController : ScreenServiceCoroutineImplBase() {
    override suspend fun sendMessage(request: SendMessageRequest): SendMessageResponse {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamScreenEvent(request: StreamScreenEventRequest): Flow<StreamScreenEventResponse> {
        TODO("NOT IMPLEMENTED")
    }
}
