package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent
import wedding.kanshasai.backend.service.RedisEventService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase

@GrpcService
class ScreenController(
    private val redisEventService: RedisEventService,
    private val grpcTool: GrpcTool,
) : ScreenServiceCoroutineImplBase() {
    override suspend fun sendMessage(request: SendMessageRequest): SendMessageResponse {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamScreenEvent(request: StreamScreenEventRequest): Flow<StreamScreenEventResponse> = callbackFlow {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        redisEventService.subscribe(CoverScreenRedisEvent::class, sessionId).collect { redisEvent ->
            val response = StreamScreenEventResponse.newBuilder().let {
                it.coverScreenEvent = it.coverScreenEventBuilder.let { event ->
                    event.type = redisEvent.coverScreenType.toGrpcType()
                    event.build()
                }
                it.build()
            }
            trySend(response)
        }
    }
}
