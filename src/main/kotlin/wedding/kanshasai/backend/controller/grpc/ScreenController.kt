package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.service.SessionService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase

@GrpcService
class ScreenController(
    private val sessionService: SessionService,
    private val grpcTool: GrpcTool,
) : ScreenServiceCoroutineImplBase() {
    override suspend fun sendMessage(request: SendMessageRequest): SendMessageResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun closeQuiz(request: CloseQuizRequest): CloseQuizResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.closeQuiz(sessionId)

        return CloseQuizResponse.newBuilder().build()
    }
}
