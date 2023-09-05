package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.*
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent
import wedding.kanshasai.backend.infra.redis.event.IntroductionRedisEvent
import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEvent
import wedding.kanshasai.backend.service.ParticipantService
import wedding.kanshasai.backend.service.RedisEventService
import wedding.kanshasai.backend.service.SessionService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
    private val sessionService: SessionService,
    private val grpcTool: GrpcTool,
    private val redisEventService: RedisEventService,
) : ParticipantServiceCoroutineImplBase() {
    override suspend fun listParticipants(request: ListParticipantsRequest): ListParticipantsResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        val participantList = participantService.listParticipantsBySessionId(sessionId)
        val grpcParticipantList = participantList.map { participant ->
            ListParticipantsResponse.Participant.newBuilder()
                .setParticipant(participant)
                .build()
        }

        return ListParticipantsResponse.newBuilder().let {
            it.addAllParticipants(grpcParticipantList)
            it.build()
        }
    }

    override suspend fun registerParticipant(request: RegisterParticipantRequest): RegisterParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun createParticipant(request: CreateParticipantRequest): CreateParticipantResponse {
        if (request.name.isNullOrEmpty()) throw InvalidArgumentException.requiredField("name")
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        val imageId = if (request.imageId.isEmpty()) {
            null
        } else {
            grpcTool.parseUlidId(request.imageId, "imageId")
        }

        val participant = participantService.createParticipant(sessionId, request.name, imageId)

        return CreateParticipantResponse.newBuilder().let {
            it.name = participant.name
            it.participantId = participant.id.toString()
            it.sessionId = participant.sessionId.toString()
            it.imageId = participant.imageId.toString()
            it.build()
        }
    }

    override suspend fun updateParticipant(request: UpdateParticipantRequest): UpdateParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun setAnswer(request: SetAnswerRequest): SetAnswerResponse {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamParticipantEvent(request: StreamParticipantEventRequest): Flow<StreamParticipantEventResponse> = callbackFlow {
        val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
        val participant = participantService.findById(participantId)

        val session = sessionService.findById(participant.sessionId)

        StreamParticipantEventResponse.newBuilder()
            .setCurrentStateEvent(
                StreamParticipantEventResponse.CurrentStateEvent.newBuilder()
                    .setIsGameInProgress(session.state.isGameInProgress())
                    .build()
            )
            .build()
            .let(::trySend)

        val subscribers = listOf(
            launch {
                redisEventService.subscribe(NextQuizRedisEvent::class, session.id).collect { redisEvent ->
                    StreamParticipantEventResponse.newBuilder()
                        .setNextQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
        )
        subscribers.joinAll()
    }
}
