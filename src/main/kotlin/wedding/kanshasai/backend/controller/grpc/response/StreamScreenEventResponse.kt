package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.event.CoverScreenRedisEvent
import wedding.kanshasai.backend.infra.redis.event.IntroductionRedisEvent
import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEvent
import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEventChoice
import wedding.kanshasai.v1.ScreenEventType
import wedding.kanshasai.v1.StreamScreenEventResponse

fun StreamScreenEventResponse.Builder.setCoverScreenEvent(event: CoverScreenRedisEvent): StreamScreenEventResponse.Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_COVER
    coverScreenEvent = this.coverScreenEventBuilder
        .setIsVisible(event.isVisible)
        .build()
}

fun StreamScreenEventResponse.Builder.setIntroductionScreenEvent(
    event:
        IntroductionRedisEvent,
): StreamScreenEventResponse.Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_INTRODUCTION
    introductionScreenEvent = this.introductionScreenEventBuilder
        .setIntroductionScreenType(event.introductionType.toGrpcType())
        .build()
}

fun StreamScreenEventResponse.Builder.setQuizEvent(event: NextQuizRedisEvent): StreamScreenEventResponse.Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_QUIZ
    quizEvent = this.quizEventBuilder
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoices(
            event.choiceList.map { choicePair ->
                StreamScreenEventResponse.Choice.newBuilder()
                    .setChoice(choicePair)
                    .build()
            },
        )
        .build()
}

fun StreamScreenEventResponse.Choice.Builder.setChoice(choicePair: NextQuizRedisEventChoice):
    StreamScreenEventResponse.Choice.Builder = apply {
    choiceId = choicePair.choiceId
    body = choicePair.body
}
