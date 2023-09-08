package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.event.*
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
    introductionEvent = this.introductionEventBuilder
        .setIntroductionType(event.introductionType.toGrpcType())
        .build()
}

fun buildQuizEvent(event: QuizRedisEvent): StreamScreenEventResponse.QuizEvent {
    return StreamScreenEventResponse.QuizEvent.newBuilder()
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoices(
            event.choiceList.map { choice ->
                StreamScreenEventResponse.Choice.newBuilder()
                    .setChoice(choice)
                    .build()
            },
        )
        .build()
}

fun StreamScreenEventResponse.Builder.setPreQuizEvent(event: NextQuizRedisEvent): StreamScreenEventResponse.Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_PRE_QUIZ
    preQuizEvent = buildQuizEvent(event)
}

fun StreamScreenEventResponse.Builder.setShowQuizEvent(event: ShowQuizRedisEvent): StreamScreenEventResponse.Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_SHOW_QUIZ
    showQuizEvent = buildQuizEvent(event)
}

fun StreamScreenEventResponse.Builder.setStartQuizEvent(event: StartQuizRedisEvent): StreamScreenEventResponse.Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_QUIZ_START
    quizStartEvent = buildQuizEvent(event)
}

fun StreamScreenEventResponse.Choice.Builder.setChoice(choicePair: QuizChoiceRedisEvent): StreamScreenEventResponse.Choice.Builder = apply {
    choiceId = choicePair.choiceId
    body = choicePair.body
}
