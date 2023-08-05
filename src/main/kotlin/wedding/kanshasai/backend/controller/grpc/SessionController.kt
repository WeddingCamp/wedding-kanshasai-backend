package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.SessionServiceGrpcKt.SessionServiceCoroutineImplBase

@GrpcService
class SessionController: SessionServiceCoroutineImplBase() {
    override suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse {
        TODO("NOT IMPLEMENTED")
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
