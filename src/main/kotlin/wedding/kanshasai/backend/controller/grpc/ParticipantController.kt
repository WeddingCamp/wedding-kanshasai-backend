package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.*
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.infra.redis.event.PreQuizRedisEvent
import wedding.kanshasai.backend.infra.redis.event.StartQuizRedisEvent
import wedding.kanshasai.backend.service.*
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
    private val sessionService: SessionService,
    private val participantAnswerService: ParticipantAnswerService,
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

        val type = ParticipantType.of(request.participantType.number)
        val participant = participantService.createParticipant(sessionId, request.name, imageId, type)

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
        val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
        val quizId = grpcTool.parseUlidId(request.quizId, "quizId")

        if (request.answer.isEmpty()) throw InvalidArgumentException.requiredField("choiceId")
        if (request.time <= 0) throw InvalidArgumentException("time must be positive number.")

        participantAnswerService.setAnswer(participantId, quizId, request.answer, request.time)

        return SetAnswerResponse.newBuilder().build()
    }

    override fun streamParticipantEvent(request: StreamParticipantEventRequest): Flow<StreamParticipantEventResponse> = callbackFlow {
        val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
        val participant = participantService.findById(participantId)

        val session = sessionService.findById(participant.sessionId)

        StreamParticipantEventResponse.newBuilder()
            .setCurrentStateEvent(
                StreamParticipantEventResponse.CurrentStateEvent.newBuilder()
                    .setGameState(session.state.getGameState())
                    .build(),
            )
            .build()
            .let(::trySend)

        val subscribers = listOf(
            launch {
                redisEventService.subscribe(PreQuizRedisEvent::class, session.id).collect { redisEvent ->
                    StreamParticipantEventResponse.newBuilder()
                        .setPreQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
            launch {
                redisEventService.subscribe(StartQuizRedisEvent::class, session.id).collect { redisEvent ->
                    StreamParticipantEventResponse.newBuilder()
                        .setStartQuizEvent(redisEvent)
                        .build()
                        .let(::trySend)
                }
            },
        )
        subscribers.joinAll()
    }
}
