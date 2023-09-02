package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.exception.DatabaseException
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.domain.value.UlidId
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
        if (request.eventId.isNullOrEmpty()) throw InvalidArgumentException.requiredField("eventId")

        val eventId = try { UlidId.of(request.eventId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'eventId' cannot be parsed as ULID format.", e)
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
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val sessionQuizList = sessionQuizService.listQuizBySessionId(sessionId).getOrElse {
            throw DatabaseException("Failed to retrieve session quizzes.", it)
        }

        val grpcSessionQuizList = sessionQuizList.map { (quiz, sessionQuiz, choiceList) ->
            ListSessionQuizzesResponse.SessionQuiz.newBuilder().let {
                it.quizId = quiz.id.toString()
                it.body = quiz.body
                it.quizType = quiz.type.toGrpcType()
                it.addAllChoices(
                    choiceList.map { choice ->
                        ListSessionQuizzesResponse.Choice.newBuilder().let { c ->
                            c.choiceId = choice.id.toString()
                            c.body = choice.body
                            c.build()
                        }
                    },
                )
                it.isCompleted = sessionQuiz.isCompleted
                it.build()
            }
        }

        return ListSessionQuizzesResponse.newBuilder().let {
            it.addAllSessionQuizzes(grpcSessionQuizList)
            it.build()
        }
    }

    override suspend fun setNextIntroduction(request: SetNextIntroductionRequest): SetNextIntroductionResponse {
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
        val introductionType = grpcTool.parseIntroductionType(request.introductionScreenType)

        sessionService.setIntroductionScreen(sessionId, introductionType).getOrElse {
            throw DatabaseException("Failed to set introduction.", it)
        }

        return SetNextIntroductionResponse.newBuilder().build()
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
        val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")

        sessionService.setCoverScreen(sessionId, request.isVisible).getOrElse {
            throw DatabaseException("Failed to set cover screen.", it)
        }

        return SetCoverScreenResponse.newBuilder().build()
    }

    override fun streamSessionEvent(request: StreamSessionEventRequest): Flow<StreamSessionEventResponse> {
        TODO("NOT IMPLEMENTED")
    }
}
