package wedding.kanshasai.backend.controller.grpc

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.ScreenServiceGrpcKt.ScreenServiceCoroutineImplBase
import wedding.kanshasai.v1.StreamScreenEventResponse.Choice
import java.util.*

private val logger = KotlinLogging.logger {}

@GrpcService
class ScreenController : ScreenServiceCoroutineImplBase() {
    override suspend fun sendMessage(request: SendMessageRequest): SendMessageResponse {
        TODO("NOT IMPLEMENTED")
    }

    override fun streamScreenEvent(request: StreamScreenEventRequest): Flow<StreamScreenEventResponse> = flow {
        while (true) {
            delay(1000)
            emit(
                StreamScreenEventResponse.newBuilder().run {
                    eventType = ScreenEventType.SCREEN_EVENT_TYPE_QUIZ
                    quizEvent = StreamScreenEventResponse.QuizEvent.newBuilder().run {
                        quizId = UUID.randomUUID().toString()
                        body = "新婦婦の実家の最寄駅は？"
                        addAllChoices(
                            listOf(
                                Choice.newBuilder().run {
                                    choiceId = UUID.randomUUID().toString()
                                    body = "日根野駅"
                                    build()
                                },
                                Choice.newBuilder().run {
                                    choiceId = UUID.randomUUID().toString()
                                    body = "泉佐野駅"
                                    build()
                                },
                                Choice.newBuilder().run {
                                    choiceId = UUID.randomUUID().toString()
                                    body = "熊取駅"
                                    build()
                                },
                                Choice.newBuilder().run {
                                    choiceId = UUID.randomUUID().toString()
                                    body = "鳳駅"
                                    build()
                                },
                            ),
                        )
                        build()
                    }
                    logger.info { "Send screen event" }
                    build()
                },
            )
        }
    }
}
