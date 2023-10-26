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

@GrpcService
class StreamController(
    private val sessionService: SessionService,
    private val participantService: ParticipantService,
    private val participantAnswerService: ParticipantAnswerService,
    private val s3Service: S3Service,
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
            RedisEvent.CurrentState::class,
            RedisEvent.ShowResultTitle::class,
            RedisEvent.ShowResultRankingTitle::class,
            RedisEvent.ShowResultRanking::class,
            RedisEvent.ShowResultPresent::class,
        ),
        StreamType.STREAM_TYPE_PARTICIPANT to listOf(
            RedisEvent.PreQuiz::class,
            RedisEvent.ShowQuiz::class,
            RedisEvent.StartQuiz::class,
            RedisEvent.QuizResult::class,
            RedisEvent.QuizTimeUp::class,
            RedisEvent.CurrentState::class,
        ),
        StreamType.STREAM_TYPE_MANAGER to listOf(
            RedisEvent.Cover::class,
            RedisEvent.Introduction::class,
            RedisEvent.PreQuiz::class,
            RedisEvent.ShowQuiz::class,
            RedisEvent.StartQuiz::class,
            RedisEvent.QuizAnswerList::class,
            RedisEvent.QuizResult::class,
            RedisEvent.QuizSpeedRanking::class,
            RedisEvent.QuizTimeUp::class,
            RedisEvent.CurrentState::class,
            RedisEvent.UpdateParticipant::class,
            RedisEvent.ShowResultTitle::class,
            RedisEvent.ShowResultRankingTitle::class,
            RedisEvent.ShowResultRanking::class,
            RedisEvent.ShowResultPresent::class,
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

        if (session.state == SessionState.INTRODUCTION) {
            StreamEventResponse.newBuilder()
                .setEventType(EventType.EVENT_TYPE_INTRODUCTION)
                .setIntroductionEvent(
                    StreamEventResponse.IntroductionEvent.newBuilder()
                        .setIntroductionId(session.currentIntroductionId)
                        .build(),
                )
                .build()
                .let(::trySend)
        }

        val eventList = map[request.type] ?: throw InvalidArgumentException.requiredField("type")

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
                                        .build()
                                }

                                is RedisEvent.AbstractQuizRedisEvent<*> -> {
                                    val selectedChoice = if (participant != null) {
                                        participantAnswerService.getAnswer(participant.id, UlidId.of(redisEvent.quizId)).getOrNull()
                                    } else { null }
                                    quiz = this.quizBuilder
                                        .setQuizId(redisEvent.quizId)
                                        .setBody(redisEvent.quizBody)
                                        .setQuizType(redisEvent.quizType)
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
                                                    .build()
                                            },
                                        )
                                        .build()
                                }

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
