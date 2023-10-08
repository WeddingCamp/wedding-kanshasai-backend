package wedding.kanshasai.backend.controller.grpc.response

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithCountRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithResultRedisEntity
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.v1.StreamEventResponse.*
import wedding.kanshasai.v1.StreamEventResponse.Quiz.Choice
import wedding.kanshasai.v1.StreamEventResponse.SpeedRanking.ParticipantQuizTime
import java.util.*

fun Builder.setRedisEvent(event: RedisEvent, s3Client: AmazonS3, s3Bucket: Bucket): Builder = apply {
    when (event) {
        is RedisEvent.Cover -> {
            cover = this.coverBuilder
                .setIsVisible(event.isVisible)
                .build()
        }

        is RedisEvent.Introduction -> {
            introductionEvent = this.introductionEventBuilder
                .setIntroductionType(event.introductionType.toGrpcType())
                .build()
        }

        is RedisEvent.AbstractQuizRedisEvent<*> -> {
            quiz = this.quizBuilder
                .setQuizId(event.quizId)
                .setBody(event.quizBody)
                .setQuizType(event.quizType)
                .addAllChoices(
                    event.choiceList.map { choice ->
                        Choice.newBuilder().let { choiceBuilder ->
                            choiceBuilder.choiceId = choice.choiceId
                            choiceBuilder.body = choice.body
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
                    event.participantQuizTimeList.map { participantQuizTime ->
                        val url = s3Client.generatePresignedUrl(s3Bucket.name, "05.jpg", Date(System.currentTimeMillis() + 1000 * 60 * 60))
                        ParticipantQuizTime.newBuilder()
                            .setParticipantName(participantQuizTime.participantName)
                            .setParticipantImageUrl(url.toString()) // TODO: 画像のURLを返すようにする
                            .setTime(participantQuizTime.time)
                            .build()
                    },
                )
                .build()
        }

        is RedisEvent.CurrentState -> {
            sessionState = this.sessionStateBuilder
                .setSimpleSessionState(event.simpleSessionState)
                .setSessionState(event.sessionState)
                .build()
        }

        is RedisEvent.UpdateParticipant -> {
            this.addAllParticipants(
                event.participantList.map {
                    val url = s3Client.generatePresignedUrl(s3Bucket.name, "05.jpg", Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    Participant.newBuilder()
                        .setParticipantId(it.participantId)
                        .setName(it.name)
                        .setImageUrl(url.toString())
                        .setParticipantType(it.participantType)
                        .setIsConnected(it.connected)
                        .build()
                },
            )
        }
    }
}
