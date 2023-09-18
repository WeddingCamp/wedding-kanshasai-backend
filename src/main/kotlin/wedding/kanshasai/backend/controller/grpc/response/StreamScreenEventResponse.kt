package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithCountRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithResultRedisEntity
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.v1.StreamEventResponse.*
import wedding.kanshasai.v1.StreamEventResponse.Quiz.Choice
import wedding.kanshasai.v1.StreamEventResponse.SpeedRanking.ParticipantQuizTime

fun Builder.setRedisEvent(event: RedisEvent): Builder = apply {
    if (event is CoverRedisEvent) {
        cover = this.coverBuilder
            .setIsVisible(event.isVisible)
            .build()
    }
    if (event is IntroductionRedisEvent) {
        introductionEvent = this.introductionEventBuilder
            .setIntroductionType(event.introductionType.toGrpcType())
            .build()
    }
    if (event is AbstractQuizRedisEvent<*>) {
        quiz = Quiz.newBuilder().let { quizBuilder ->
            quizBuilder.quizId = event.quizId
            quizBuilder.body = event.quizBody
            quizBuilder.quizType = event.quizType
            quizBuilder.addAllChoices(
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
            quizBuilder.build()
        }
    }
    if (event is QuizSpeedRankingRedisEvent) {
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
}
