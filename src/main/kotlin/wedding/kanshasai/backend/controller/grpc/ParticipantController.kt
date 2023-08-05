package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController: ParticipantServiceCoroutineImplBase() {
    override suspend fun listParticipant(request: ListParticipantRequest): ListParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun registerParticipant(request: RegisterParticipantRequest): RegisterParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun createParticipant(request: CreateParticipantRequest): CreateParticipantResponse {
        TODO("NOT IMPLEMENTED")
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
