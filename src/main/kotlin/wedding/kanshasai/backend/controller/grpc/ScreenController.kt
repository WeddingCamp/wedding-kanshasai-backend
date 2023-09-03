package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.setCoverScreenEvent
import wedding.kanshasai.backend.controller.grpc.response.setIntroductionScreenEvent
import wedding.kanshasai.backend.controller.grpc.response.setQuizEvent
import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent
import wedding.kanshasai.backend.infra.redis.event.IntroductionRedisEvent
import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEvent
import wedding.kanshasai.backend.service.ChoiceService
import wedding.kanshasai.backend.service.RedisEventService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase

@GrpcService
class ScreenController(
    private val redisEventService: RedisEventService,
    private val choiceService: ChoiceService,
    private val grpcTool: GrpcTool,
) : ScreenServiceCoroutineImplBase() {
    override suspend fun sendMessage(request: SendMessageRequest): SendMessageResponse {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamScreenEvent(request: StreamScreenEventRequest): Flow<StreamScreenEventResponse> = callbackFlow {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val subscribers = listOf(
            launch {
                redisEventService.subscribe(CoverScreenRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setCoverScreenEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(IntroductionRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setIntroductionScreenEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(NextQuizRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
        )
        subscribers.joinAll()
    }
}
