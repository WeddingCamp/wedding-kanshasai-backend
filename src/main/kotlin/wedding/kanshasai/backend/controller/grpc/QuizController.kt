package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.QuizServiceGrpcKt.QuizServiceCoroutineImplBase

@GrpcService
class QuizController : QuizServiceCoroutineImplBase() {
    override suspend fun listQuizzes(request: ListQuizzesRequest): ListQuizzesResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun createQuiz(request: CreateQuizRequest): CreateQuizResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun updateQuiz(request: UpdateQuizRequest): UpdateQuizResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun deleteQuiz(request: DeleteQuizRequest): DeleteQuizResponse {
        TODO("NOT IMPLEMENTED")
    }
}
