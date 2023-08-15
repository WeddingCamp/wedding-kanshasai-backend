package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.QuizServiceGrpcKt.QuizServiceCoroutineImplBase
import java.util.UUID

@GrpcService
class QuizController : QuizServiceCoroutineImplBase() {
    override suspend fun listQuizzes(request: ListQuizzesRequest): ListQuizzesResponse {
        return ListQuizzesResponse.newBuilder().run {
            addAllQuizzes(
                listOf(
                    ListQuizzesResponse.Quiz.newBuilder().run {
                        quizId = UUID.randomUUID().toString()
                        body = "新婦婦の実家の最寄駅は？"
                        addAllChoices(listOf("日根野駅", "泉佐野駅", "熊取駅", "鳳駅"))
                        correctAnswer = "{\"answer_id\": 1}"
                        build()
                    },
                    ListQuizzesResponse.Quiz.newBuilder().run {
                        quizId = UUID.randomUUID().toString()
                        body = "最寄りのスーパーはどこ？"
                        addAllChoices(listOf("ライフ", "イオン", "イズミヤ", "イトーヨーカドー"))
                        correctAnswer = "{\"answer_id\": 1}"
                        build()
                    },
                ),
            )
            build()
        }
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
