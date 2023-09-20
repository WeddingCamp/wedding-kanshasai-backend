package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.*
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.backend.service.ParticipantService
import wedding.kanshasai.backend.service.RedisEventService
import wedding.kanshasai.backend.service.SessionService
import wedding.kanshasai.v1.StreamEventRequest
import wedding.kanshasai.v1.StreamEventResponse
import wedding.kanshasai.v1.StreamServiceGrpcKt
import wedding.kanshasai.v1.StreamType

@GrpcService
class StreamController(
    private val sessionService: SessionService,
    private val participantService: ParticipantService,
    private val grpcTool: GrpcTool,
    private val redisEventService: RedisEventService,
) : StreamServiceGrpcKt.StreamServiceCoroutineImplBase() {

    val map = mapOf(
        StreamType.STREAM_TYPE_SCREEN to listOf(
            CoverRedisEvent::class,
            IntroductionRedisEvent::class,
            PreQuizRedisEvent::class,
            ShowQuizRedisEvent::class,
            StartQuizRedisEvent::class,
            QuizAnswerListRedisEvent::class,
            QuizResultRedisEvent::class,
            QuizSpeedRankingRedisEvent::class,
        ),
        StreamType.STREAM_TYPE_PARTICIPANT to listOf(
            PreQuizRedisEvent::class,
            ShowQuizRedisEvent::class,
            StartQuizRedisEvent::class,
            QuizResultRedisEvent::class,
        ),
        StreamType.STREAM_TYPE_MANAGER to listOf(
            CoverRedisEvent::class,
            IntroductionRedisEvent::class,
            PreQuizRedisEvent::class,
            ShowQuizRedisEvent::class,
            StartQuizRedisEvent::class,
            QuizAnswerListRedisEvent::class,
            QuizResultRedisEvent::class,
            QuizSpeedRankingRedisEvent::class,
        ),
        StreamType.STREAM_TYPE_DEBUG to listOf(
            CoverRedisEvent::class,
            IntroductionRedisEvent::class,
            PreQuizRedisEvent::class,
            ShowQuizRedisEvent::class,
            StartQuizRedisEvent::class,
            QuizAnswerListRedisEvent::class,
            QuizResultRedisEvent::class,
            QuizSpeedRankingRedisEvent::class,
        ),
    )

    override fun streamEvent(request: StreamEventRequest): Flow<StreamEventResponse> = callbackFlow {
        val participant = if (listOf(StreamType.STREAM_TYPE_PARTICIPANT).contains(request.type)) {
            val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
            participantService.findById(participantId)
        } else {
            null
        }

        val session = if (participant != null) {
            sessionService.findById(participant.sessionId)
        } else {
            val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
            sessionService.findById(sessionId)
        }

        StreamEventResponse.newBuilder()
            .setSessionState(
                StreamEventResponse.SessionState.newBuilder()
                    .setSimpleSessionState(session.state.getSimpleSessionState())
                    .build(),
            )
            .build()
            .let(::trySend)

        val eventList = map[request.type] ?: throw InvalidArgumentException.requiredField("type")

        val subscribers = eventList.map {
            launch {
                redisEventService.subscribe(it, session.id).collect { redisEvent ->
                    StreamEventResponse.newBuilder()
                        .setEventType(redisEvent.eventType)
                        .setRedisEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            }
        }
        subscribers.joinAll()
    }
}
