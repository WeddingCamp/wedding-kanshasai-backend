package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.*
import wedding.kanshasai.backend.infra.redis.event.*
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

    override suspend fun finishQuiz(request: FinishQuizRequest): FinishQuizResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.finishQuiz(sessionId)

        return FinishQuizResponse.newBuilder().build()
    }
}
