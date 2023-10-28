package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.service.*
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
    private val participantAnswerService: ParticipantAnswerService,
    private val s3Service: S3Service,
    private val grpcTool: GrpcTool,
) : ParticipantServiceCoroutineImplBase() {
    fun ListParticipantsResponse.Participant.Builder.setParticipant(participant: Participant):
        ListParticipantsResponse.Participant.Builder = apply {
        name = participant.name
        participantId = participant.id.toString()
        sessionId = participant.sessionId.toString()
        imageUrl = s3Service.generatePresignedUrl(participant.imageId)
        participantType = participant.type.toGrpcType()
    }

    override suspend fun getParticipant(request: GetParticipantRequest): GetParticipantResponse {
        val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
        val participant = participantService.findById(participantId)

        return GetParticipantResponse.newBuilder().let {
            it.name = participant.name
            it.participantId = participant.id.toString()
            it.sessionId = participant.sessionId.toString()
            it.imageUrl = s3Service.generatePresignedUrl(participant.imageId)
            it.participantType = participant.type.toGrpcType()
            it.build()
        }
    }

    override suspend fun listParticipants(request: ListParticipantsRequest): ListParticipantsResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val participantType = if (request.hasParticipantType()) ParticipantType.of(request.participantType.number) else null

        val participantList = participantService.listParticipantsBySessionId(sessionId)
        val grpcParticipantList = participantList
            .filter { participantType == null || it.type == participantType }
            .map { participant ->
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
            it.imageUrl = s3Service.generatePresignedUrl(participant.imageId)
            it.build()
        }
    }

    override suspend fun updateParticipant(request: UpdateParticipantRequest): UpdateParticipantResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun setAnswer(request: SetAnswerRequest): SetAnswerResponse {
        val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
        val quizId = grpcTool.parseUlidId(request.quizId, "quizId")

        if (request.answer.isEmpty()) throw InvalidArgumentException.requiredField("answer")
        if (request.time <= 0) throw InvalidArgumentException("time must be positive number.")

        participantAnswerService.setAnswer(participantId, quizId, request.answer, request.time)

        return SetAnswerResponse.newBuilder().build()
    }
}
