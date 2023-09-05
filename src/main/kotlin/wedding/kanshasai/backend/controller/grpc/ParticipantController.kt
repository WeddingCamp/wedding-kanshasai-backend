package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.setParticipant
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.service.ParticipantService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
    private val grpcTool: GrpcTool,
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

    override fun streamParticipantEvent(request: StreamParticipantEventRequest): Flow<StreamParticipantEventResponse> {
        TODO("NOT IMPLEMENTED")
    }
}
