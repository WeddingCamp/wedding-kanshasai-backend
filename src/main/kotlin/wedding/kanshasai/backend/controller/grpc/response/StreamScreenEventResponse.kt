package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithCountRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithResultRedisEntity
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.v1.StreamEventResponse.*
import wedding.kanshasai.v1.StreamEventResponse.Quiz.Choice
import wedding.kanshasai.v1.StreamEventResponse.SpeedRanking.ParticipantQuizTime

fun Builder.setRedisEvent(event: RedisEvent): Builder = apply {
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
                        ParticipantQuizTime.newBuilder()
                            .setParticipantName(participantQuizTime.participantName)
                            .setParticipantImageUrl(participantQuizTime.participantImageId) // TODO: 画像のURLを返すようにする
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
                    Participant.newBuilder()
                        .setParticipantId(it.participantId)
                        .setName(it.name)
                        .setImageUrl(it.imageUrl)
                        .setParticipantType(it.participantType)
                        .setIsConnected(it.connected)
                        .build()
                },
            )
        }
    }
}
