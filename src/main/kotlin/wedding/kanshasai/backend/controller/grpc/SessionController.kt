package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.service.SessionService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.SessionServiceGrpcKt.SessionServiceCoroutineImplBase

@GrpcService
class SessionController(
    private val sessionService: SessionService,
) : SessionServiceCoroutineImplBase() {
    override suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse {
        if (request.name.isEmpty()) throw InvalidArgumentException("'name' is required.")
        if (request.eventId.isEmpty()) throw InvalidArgumentException("'eventId' is required.")

        val eventId = UlidId.of(request.eventId).getOrElse {
            throw InvalidArgumentException("'eventId' cannot be parsed as ULID format.", it)
        }

        val session = sessionService.createSession(eventId, request.name).getOrThrow()

        return CreateSessionResponse.newBuilder().let {
            it.name = session.name
            it.sessionId = session.id.toString()
            it.eventId = session.eventId.toString()
            it.build()
        }
    }

    override suspend fun listSessionQuizzes(request: ListSessionQuizzesRequest): ListSessionQuizzesResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun setNextQuiz(request: SetNextQuizRequest): SetNextQuizResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun startQuiz(request: StartQuizRequest): StartQuizResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun showQuizResult(request: ShowQuizResultRequest): ShowQuizResultResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun cancelCurrentQuiz(request: CancelCurrentQuizRequest): CancelCurrentQuizResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun showSessionResult(request: ShowSessionResultRequest): ShowSessionResultResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun setCoverScreen(request: SetCoverScreenRequest): SetCoverScreenResponse {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamSessionEvent(request: StreamSessionEventRequest): Flow<StreamSessionEventResponse> {
        TODO("NOT IMPLEMENTED")
    }
}
