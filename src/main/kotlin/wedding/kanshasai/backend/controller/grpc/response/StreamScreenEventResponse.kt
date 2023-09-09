package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.entity.AbstractQuizChoiceRedisEntity
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceRedisEntity
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.v1.ScreenEventType
import wedding.kanshasai.v1.StreamScreenEventResponse.*

fun Builder.setCoverScreenEvent(event: CoverScreenRedisEvent): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_COVER
    coverScreenEvent = this.coverScreenEventBuilder
        .setIsVisible(event.isVisible)
        .build()
}

fun Builder.setIntroductionScreenEvent(
    event:
        IntroductionRedisEvent,
): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_INTRODUCTION
    introductionEvent = this.introductionEventBuilder
        .setIntroductionType(event.introductionType.toGrpcType())
        .build()
}

fun buildQuizEvent(event: AbstractQuizRedisEvent<QuizChoiceRedisEntity>): QuizEvent {
    return QuizEvent.newBuilder()
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoices(
            event.choiceList.map { choice ->
                Choice.newBuilder()
                    .setChoice(choice)
                    .build()
            },
        )
        .build()
}

fun buildQuizChoiceResultEvent(event: QuizVoteListRedisEvent): QuizChoiceResultEvent {
    return QuizChoiceResultEvent.newBuilder()
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoicesWithCount(
            event.choiceList.map { choice ->
                QuizChoiceResultEvent.ChoiceWithCount.newBuilder()
                    .setChoiceId(choice.choiceId)
                    .setBody(choice.body)
                    .setCount(choice.count)
                    .build()
            },
        )
        .build()
}

fun buildQuizResultEvent(event: QuizResultRedisEvent): QuizResultEvent {
    return QuizResultEvent.newBuilder()
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoicesWithResult(
            event.choiceList.map { choice ->
                QuizResultEvent.ChoiceWithResult.newBuilder()
                    .setChoiceId(choice.choiceId)
                    .setBody(choice.body)
                    .setCount(choice.count)
                    .setIsCorrectChoice(choice.correctChoice)
                    .build()
            },
        )
        .build()
}

fun Builder.setPreQuizEvent(event: PreQuizRedisEvent): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_PRE_QUIZ
    preQuizEvent = buildQuizEvent(event)
}

fun Builder.setShowQuizEvent(event: ShowQuizRedisEvent): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_SHOW_QUIZ
    showQuizEvent = buildQuizEvent(event)
}

fun Builder.setStartQuizEvent(event: StartQuizRedisEvent): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_QUIZ_START
    quizStartEvent = buildQuizEvent(event)
}

fun Builder.setQuizVoteListEvent(event: QuizVoteListRedisEvent): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_QUIZ_ANSWER_LIST
    quizChoiceResultEvent = buildQuizChoiceResultEvent(event)
}

fun Builder.setQuizResultEvent(event: QuizResultRedisEvent): Builder = apply {
    screenEventType = ScreenEventType.SCREEN_EVENT_TYPE_QUIZ_RESULT
    quizResultEvent = buildQuizResultEvent(event)
}

fun Choice.Builder.setChoice(choicePair: AbstractQuizChoiceRedisEntity): Choice.Builder = apply {
    choiceId = choicePair.choiceId
    body = choicePair.body
}
