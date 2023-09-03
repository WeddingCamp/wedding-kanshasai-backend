package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.setParticipant
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.service.ParticipantService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
) : ParticipantServiceCoroutineImplBase() {
    override suspend fun listParticipants(request: ListParticipantsRequest): ListParticipantsResponse {
        if (request.sessionId.isNullOrEmpty()) throw InvalidArgumentException.requiredField("sessionId")

        val sessionId = try { UlidId.of(request.sessionId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'sessionId' cannot be parsed as ULID format.", e)
        }

        val participantList = participantService.listParticipantsBySessionId(sessionId).getOrThrow()
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
        if (request.sessionId.isNullOrEmpty()) throw InvalidArgumentException.requiredField("sessionId")

        val imageId = if (request.imageId.isEmpty()) {
            null
        } else {
            try { UlidId.of(request.imageId) } catch (e: InvalidValueException) {
                throw InvalidArgumentException("'imageId' cannot be parsed as ULID format.", e)
            }
        }
        val sessionId = try { UlidId.of(request.sessionId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'sessionId' cannot be parsed as ULID format.", e)
        }

        val participant = participantService.createParticipant(sessionId, request.name, imageId).getOrThrow()

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
