package wedding.kanshasai.backend.controller.grpc

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithCountRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithResultRedisEntity
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.backend.service.*
import wedding.kanshasai.v1.*
import java.sql.Timestamp
import java.util.*

@GrpcService
class StreamController(
    private val sessionService: SessionService,
    private val participantService: ParticipantService,
    private val participantAnswerService: ParticipantAnswerService,
    private val s3Service: S3Service,
    private val qrCodeService: QrCodeService,
    private val grpcTool: GrpcTool,
    private val redisEventService: RedisEventService,
) : StreamServiceGrpcKt.StreamServiceCoroutineImplBase() {

    val map = mapOf(
        StreamType.STREAM_TYPE_SCREEN to listOf(
            RedisEvent.Cover::class,
            RedisEvent.Introduction::class,
            RedisEvent.PreQuiz::class,
            RedisEvent.ShowQuiz::class,
            RedisEvent.StartQuiz::class,
            RedisEvent.QuizAnswerList::class,
            RedisEvent.QuizResult::class,
            RedisEvent.QuizSpeedRanking::class,
            RedisEvent.QuizTimeUp::class,
            RedisEvent.CancelQuiz::class,
            RedisEvent.CurrentState::class,
            RedisEvent.ShowResultTitle::class,
            RedisEvent.ShowResultRankingTitle::class,
            RedisEvent.ShowResultRanking::class,
            RedisEvent.ShowResultPresent::class,
            RedisEvent.Finish::class,
        ),
        StreamType.STREAM_TYPE_PARTICIPANT to listOf(
            RedisEvent.ShowProfile::class,
            RedisEvent.PreQuiz::class,
            RedisEvent.ShowQuiz::class,
            RedisEvent.StartQuiz::class,
            RedisEvent.QuizResult::class,
            RedisEvent.QuizTimeUp::class,
            RedisEvent.CancelQuiz::class,
            RedisEvent.ShowResultTitle::class,
            RedisEvent.FullCurrentState::class,
            RedisEvent.Finish::class,
        ),
        StreamType.STREAM_TYPE_MANAGER to listOf(
            RedisEvent.Cover::class,
            RedisEvent.Introduction::class,
            RedisEvent.ShowProfile::class,
            RedisEvent.PreQuiz::class,
            RedisEvent.ShowQuiz::class,
            RedisEvent.StartQuiz::class,
            RedisEvent.QuizAnswerList::class,
            RedisEvent.QuizResult::class,
            RedisEvent.QuizSpeedRanking::class,
            RedisEvent.QuizTimeUp::class,
            RedisEvent.CancelQuiz::class,
            RedisEvent.CurrentState::class,
            RedisEvent.FullCurrentState::class,
            RedisEvent.UpdateParticipant::class,
            RedisEvent.ShowResultTitle::class,
            RedisEvent.ShowResultRankingTitle::class,
            RedisEvent.ShowResultRanking::class,
            RedisEvent.ShowResultPresent::class,
            RedisEvent.Finish::class,
        ),
    )

    override fun streamEvent(request: StreamEventRequest): Flow<StreamEventResponse> = callbackFlow {
        val participant = if (listOf(StreamType.STREAM_TYPE_PARTICIPANT).contains(request.type)) {
            val participantId = grpcTool.parseUlidId(request.participantId, "participantId")
            participantService.setConnected(participantId, true)
            participantService.findById(participantId)
        } else {
            null
        }

        val session = if (participant != null) {
            sessionService.findById(participant.sessionId)
        } else {
            val sessionId = grpcTool.parseUlidId(request.sessionId, "sessionId")
            sessionService.findById(sessionId)
        }

        val eventList = map[request.type] ?: throw InvalidArgumentException.requiredField("type")

        if (participant != null) {
            StreamEventResponse.newBuilder()
                .setEventType(EventType.EVENT_TYPE_FULL_CURRENT_STATE)
                .setSessionState(
                    StreamEventResponse.SessionState.newBuilder()
                        .setSessionState(session.state.toString())
                        .setSimpleSessionState(session.state.toSimpleSessionState())
                        .build(),
                )
                .apply {
                    if (
                        session.state == SessionState.QUIZ_WAITING ||
                        session.state == SessionState.QUIZ_SHOWING ||
                        session.state == SessionState.QUIZ_PLAYING ||
                        session.state == SessionState.QUIZ_CLOSED ||
                        session.state == SessionState.QUIZ_RESULT
                    ) {
                        sessionService.getCurrentQuiz(session.id).onSuccess { currentQuiz ->
                            quiz = this.quizBuilder
                                .setQuizId(currentQuiz.first.id.toString())
                                .setBody(currentQuiz.first.body)
                                .setQuizType(currentQuiz.first.type.toGrpcType())
                                .addAllChoices(
                                    currentQuiz.second.map { choice ->
                                        StreamEventResponse.Quiz.Choice.newBuilder().let { choiceBuilder ->
                                            choiceBuilder.choiceId = choice.id.toString()
                                            choiceBuilder.body = redisEventService.parseBody(choice.body, currentQuiz.first.type)
                                            choiceBuilder.isSelectedChoice = participantAnswerService.getAnswer(
                                                participant.id,
                                                currentQuiz.first.id,
                                            ).getOrNull()?.answer == choice.id.toString()

                                            if (session.state == SessionState.QUIZ_RESULT) {
                                                choiceBuilder.isCorrectChoice = currentQuiz.first
                                                    .isCorrectAnswer(currentQuiz.third, choice.id.toString())
                                            }
                                            choiceBuilder.build()
                                        }
                                    },
                                )
                                .apply {
                                    if (session.state == SessionState.QUIZ_PLAYING) {
                                        val startAt = currentQuiz.third.startedAt?.time
                                        elapsedTime = if (startAt == null) {
                                            0f
                                        } else {
                                            val diffMillis = Timestamp.from(Date().toInstant()).time - startAt
                                            (diffMillis / 1000).toFloat()
                                        }
                                    }
                                }
                                .build()
                        }
                    }
                }
                .build()
                .let(::trySend)
        } else if (eventList.contains(RedisEvent.CurrentState::class)) {
            StreamEventResponse.newBuilder()
                .setEventType(EventType.EVENT_TYPE_CURRENT_STATE)
                .setSessionState(
                    StreamEventResponse.SessionState.newBuilder()
                        .setSessionState(session.state.toString())
                        .setSimpleSessionState(session.state.toSimpleSessionState())
                        .build(),
                )
                .build()
                .let(::trySend)
        }

        if (session.state == SessionState.INTRODUCTION) {
            val url = qrCodeService.generateQrCodeUrl(session)
            StreamEventResponse.newBuilder()
                .setEventType(EventType.EVENT_TYPE_INTRODUCTION)
                .setIntroductionEvent(
                    StreamEventResponse.IntroductionEvent.newBuilder()
                        .setIntroductionId(session.currentIntroductionId)
                        .setQrCodeImageUrl(url)
                        .build(),
                )
                .build()
                .let(::trySend)
        }

        eventList.forEach {
            launch {
                redisEventService.subscribe(it, session.id).collect { redisEvent ->
                    StreamEventResponse.newBuilder()
                        .setEventType(redisEvent.eventType)
                        .apply {
                            when (redisEvent) {
                                is RedisEvent.Cover -> {
                                    cover = this.coverBuilder
                                        .setIsVisible(redisEvent.isVisible)
                                        .build()
                                }

                                is RedisEvent.Introduction -> {
                                    introductionEvent = this.introductionEventBuilder
                                        .setIntroductionId(redisEvent.introductionId)
                                        .setIsFirst(redisEvent.isFirst)
                                        .setIsLast(redisEvent.isLast)
                                        .setQrCodeImageUrl(redisEvent.qrCodeImageUrl)
                                        .build()
                                }

                                is RedisEvent.ShowProfile -> {
                                    if (participant != null) {
                                        profile = this.profileBuilder
                                            .setParticipantName(participant.name)
                                            .setParticipantImageUrl(
                                                s3Service.generatePresignedUrl(participant.imageId),
                                            )
                                            .build()
                                    }
                                }

                                is RedisEvent.AbstractQuizRedisEvent<*> -> {
                                    val selectedChoice = if (participant != null) {
                                        participantAnswerService.getAnswer(participant.id, UlidId.of(redisEvent.quizId)).getOrNull()
                                    } else { null }
                                    quiz = this.quizBuilder
                                        .setQuizId(redisEvent.quizId)
                                        .setBody(redisEvent.quizBody)
                                        .setQuizType(redisEvent.quizType)
                                        .setQuizNumber(redisEvent.quizNumber)
                                        .addAllChoices(
                                            redisEvent.choiceList.map { choice ->
                                                StreamEventResponse.Quiz.Choice.newBuilder().let { choiceBuilder ->
                                                    choiceBuilder.choiceId = choice.choiceId
                                                    choiceBuilder.body = choice.body
                                                    if (request.type == StreamType.STREAM_TYPE_PARTICIPANT) {
                                                        choiceBuilder.isSelectedChoice = selectedChoice?.answer == choice.choiceId
                                                    }
                                                    if (choice is QuizChoiceWithCountRedisEntity) {
                                                        choiceBuilder.count = choice.count
                                                    }
                                                    if (choice is QuizChoiceWithResultRedisEntity) {
                                                        choiceBuilder.count = choice.count
                                                        choiceBuilder.isCorrectChoice = choice.correctChoice
                                                    }
                                                    choiceBuilder.build()
                                                }
                                            },
                                        )
                                        .build()
                                }

                                is RedisEvent.QuizSpeedRanking -> {
                                    speedRanking = this.speedRankingBuilder
                                        .addAllParticipantQuizTimes(
                                            redisEvent.participantQuizTimeList.map { participantQuizTime ->
                                                StreamEventResponse.SpeedRanking.ParticipantQuizTime.newBuilder()
                                                    .setParticipantName(participantQuizTime.participantName)
                                                    .setParticipantImageUrl(
                                                        s3Service.generatePresignedUrl(participantQuizTime.participantImageId),
                                                    )
                                                    .setTime(participantQuizTime.time)
                                                    .setRank(participantQuizTime.rank)
                                                    .build()
                                            },
                                        )
                                        .build()
                                }

                                is RedisEvent.CancelQuiz -> {}

                                is RedisEvent.CurrentState -> {
                                    sessionState = this.sessionStateBuilder
                                        .setSimpleSessionState(redisEvent.simpleSessionState)
                                        .setSessionState(redisEvent.sessionState)
                                        .build()
                                }

                                is RedisEvent.UpdateParticipant -> {
                                    this.addAllParticipants(
                                        redisEvent.participantList.map {
                                            StreamEventResponse.Participant.newBuilder()
                                                .setParticipantId(it.participantId)
                                                .setName(it.name)
                                                .setImageUrl(it.imageUrl)
                                                .setParticipantType(it.participantType)
                                                .setIsConnected(it.connected)
                                                .build()
                                        },
                                    )
                                }

                                is RedisEvent.ShowResultTitle -> {
                                    resultTitle = this.resultTitleBuilder
                                        .setResultType(redisEvent.resultType)
                                        .build()
                                }

                                is RedisEvent.ShowResultRankingTitle -> {
                                    resultRankingTitle = this.resultRankingTitleBuilder
                                        .setRank(redisEvent.rank)
                                        .setResultTitleType(redisEvent.resultTitleType)
                                        .build()
                                }

                                is RedisEvent.ShowResultRanking -> {
                                    resultRanking = this.resultRankingBuilder
                                        .addAllParticipantSessionScores(
                                            redisEvent.participantSessionScoreList.map { participantSessionScore ->
                                                StreamEventResponse.ResultRanking.ParticipantSessionScore.newBuilder()
                                                    .setParticipantName(participantSessionScore.participantName)
                                                    .setScore(participantSessionScore.score)
                                                    .setIsEmphasis(participantSessionScore.isEmphasis)
                                                    .setRank(participantSessionScore.rank)
                                                    .setTime(participantSessionScore.time)
                                                    .build()
                                            },
                                        )
                                        .setPreDisplayCount(redisEvent.preDisplayCount)
                                        .setDisplayCount(redisEvent.displayCount)
                                        .setResultRankingType(redisEvent.resultRankingType)
                                        .setHasNextPage(redisEvent.hasNextPage)
                                        .build()
                                }

                                is RedisEvent.ShowResultPresent -> {
                                    resultPresent = this.resultPresentBuilder
                                        .setRank(redisEvent.rank)
                                        .setResultPresentType(redisEvent.resultRankingType)
                                        .build()
                                }
                            }
                        }
                        .build()
                        .let(::trySend)
                }
            }
        }

        this.awaitClose {
            if (participant?.id != null) {
                participantService.setConnected(participant.id, false)
            }
        }
    }
}
