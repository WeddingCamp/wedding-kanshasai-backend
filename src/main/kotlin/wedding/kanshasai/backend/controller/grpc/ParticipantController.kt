package wedding.kanshasai.backend.controller.grpc

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.*
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.ParticipantType
import wedding.kanshasai.backend.service.*
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase
import java.util.*

@GrpcService
class ParticipantController(
    private val participantService: ParticipantService,
    private val participantAnswerService: ParticipantAnswerService,
    private val grpcTool: GrpcTool,
    private val redisEventService: RedisEventService,
    private val s3Client: AmazonS3,
    private val s3Bucket: Bucket,
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
            if (participant.imageId != null) it.imageUrl = participant.imageId.toString() // TODO: 画像のURLを返す
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
}
