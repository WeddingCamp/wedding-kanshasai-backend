package wedding.kanshasai.backend.controller.grpc

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent
import wedding.kanshasai.backend.infra.redis.event.IntroductionRedisEvent
import wedding.kanshasai.backend.service.RedisEventService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase

val logger = KotlinLogging.logger {}

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
        val subscribers = listOf(
            launch {
                redisEventService.subscribe(CoverScreenRedisEvent::class, sessionId).collect { redisEvent ->
                    val response = StreamScreenEventResponse.newBuilder().let {
                        it.screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_COVER
                        it.coverScreenEvent = it.coverScreenEventBuilder.let { event ->
                            event.isVisible = redisEvent.isVisible
                            event.build()
                        }
                        it.build()
                    }
                    trySend(response)
                }
            },
            launch {
                redisEventService.subscribe(IntroductionRedisEvent::class, sessionId).collect { redisEvent ->
                    val response = StreamScreenEventResponse.newBuilder().let {
                        it.screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_INTRODUCTION
                        it.introductionScreenEvent = it.introductionScreenEventBuilder.let { event ->
                            event.introductionScreenType = redisEvent.introductionType.toGrpcType()
                            event.build()
                        }
                        it.build()
                    }
                    logger.debug { "Send introduction screen event: $redisEvent" }
                    trySend(response)
                }
            },
        )
        subscribers.joinAll()
    }
}
