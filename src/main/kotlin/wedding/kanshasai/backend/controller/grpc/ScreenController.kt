package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.*
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.backend.service.RedisEventService
import wedding.kanshasai.backend.service.SessionService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase

@GrpcService
class ScreenController(
    private val redisEventService: RedisEventService,
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
                redisEventService.subscribe(PreQuizRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setPreQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(ShowQuizRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setShowQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(StartQuizRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setStartQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(QuizVoteListRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setQuizVoteListEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(QuizResultRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setQuizResultEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(QuizFastestRankingRedisEvent::class, sessionId).collect { redisEvent ->
                    StreamScreenEventResponse.newBuilder()
                        .setQuizFastestRankingEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
        )
        subscribers.joinAll()
    }
}
