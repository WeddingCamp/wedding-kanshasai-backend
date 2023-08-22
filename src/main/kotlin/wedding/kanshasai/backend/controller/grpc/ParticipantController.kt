package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.service.ParticipantService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
) : ParticipantServiceCoroutineImplBase() {
    override suspend fun listParticipant(request: ListParticipantRequest): ListParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun registerParticipant(request: RegisterParticipantRequest): RegisterParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun createParticipant(request: CreateParticipantRequest): CreateParticipantResponse {
        if (request.name.isNullOrEmpty()) throw InvalidArgumentException.requiredField("name")
        if (request.sessionId.isNullOrEmpty()) throw InvalidArgumentException.requiredField("sessionId")

        val imageId = UlidId.of(request.imageId).getOrElse {
            throw InvalidArgumentException("'imageId' cannot be parsed as ULID format.", it)
        }
        val sessionId = UlidId.of(request.sessionId).getOrElse {
            throw InvalidArgumentException("'eventId' cannot be parsed as ULID format.", it)
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
