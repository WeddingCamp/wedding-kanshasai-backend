package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.controller.grpc.response.setChoice
import wedding.kanshasai.backend.controller.grpc.response.setQuiz
import wedding.kanshasai.backend.controller.grpc.response.setSession
import wedding.kanshasai.backend.controller.grpc.response.setSessionQuiz
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.service.SessionQuizService
import wedding.kanshasai.backend.service.SessionService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.SessionServiceGrpcKt.SessionServiceCoroutineImplBase

@GrpcService
class SessionController(
    private val sessionService: SessionService,
    private val grpcTool: GrpcTool,
    private val sessionQuizService: SessionQuizService,
) : SessionServiceCoroutineImplBase() {
    override suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse {
        if (request.name.isNullOrEmpty()) throw InvalidArgumentException.requiredField("name")
        val eventId = grpcTool.parseUlidId(request.eventId, "eventId")

        val session = sessionService.createSession(eventId, request.name)

        return CreateSessionResponse.newBuilder()
            .setSession(session)
            .build()
    }

    override suspend fun listSessions(request: ListSessionsRequest): ListSessionsResponse {
        val eventId = grpcTool.parseUlidId(request.eventId, "eventId")
        val includeFinished = request.includeFinished

        val sessionList = sessionService.listSessions(eventId, includeFinished)
            .map {
                ListSessionsResponse.Session.newBuilder()
                    .setSession(it)
                    .build()
            }

        return ListSessionsResponse.newBuilder()
            .addAllSessions(sessionList)
            .build()
    }

    override suspend fun listSessionQuizzes(request: ListSessionQuizzesRequest): ListSessionQuizzesResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        val sessionQuizList = sessionQuizService.listQuizBySessionId(sessionId)

        val grpcSessionQuizList = sessionQuizList.map { (quiz, sessionQuiz, choiceList) ->
            val grpcChoiceList = choiceList.map { choice ->
                ListSessionQuizzesResponse.Choice.newBuilder()
                    .setChoice(choice)
                    .build()
            }
            ListSessionQuizzesResponse.SessionQuiz.newBuilder()
                .setQuiz(quiz)
                .setSessionQuiz(sessionQuiz)
                .addAllChoices(grpcChoiceList)
                .build()
        }
        return ListSessionQuizzesResponse.newBuilder()
            .addAllSessionQuizzes(grpcSessionQuizList)
            .build()
    }

    override suspend fun setIntroduction(request: SetIntroductionRequest): SetIntroductionResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val introductionType = grpcTool.parseIntroductionType(request.introductionType)

        sessionService.setIntroductionScreen(sessionId, introductionType)

        return SetIntroductionResponse.newBuilder().build()
    }

    override suspend fun setNextQuiz(request: SetNextQuizRequest): SetNextQuizResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val quizId = grpcTool.parseUlidId(request.quizId, "quizId")

        sessionService.setNextQuiz(sessionId, quizId)

        return SetNextQuizResponse.newBuilder().build()
    }

    override suspend fun showQuiz(request: ShowQuizRequest): ShowQuizResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.showQuiz(sessionId)

        return ShowQuizResponse.newBuilder().build()
    }

    override suspend fun startQuiz(request: StartQuizRequest): StartQuizResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.startQuiz(sessionId)

        return StartQuizResponse.newBuilder().build()
    }

    override suspend fun showQuizResult(request: ShowQuizResultRequest): ShowQuizResultResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val quizResultType = grpcTool.parseQuizResultType(request.quizResultType)

        sessionService.showQuizResult(sessionId, quizResultType)

        return ShowQuizResultResponse.newBuilder().build()
    }

    override suspend fun cancelCurrentQuiz(request: CancelCurrentQuizRequest): CancelCurrentQuizResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.cancelCurrentQuiz(sessionId)

        return CancelCurrentQuizResponse.newBuilder().build()
    }

    override suspend fun showSessionResult(request: ShowSessionResultRequest): ShowSessionResultResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun setCover(request: SetCoverRequest): SetCoverResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.setCoverScreen(sessionId, request.isVisible)

        return SetCoverResponse.newBuilder().build()
    }
}
