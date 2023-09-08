package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.event.PreQuizRedisEvent
import wedding.kanshasai.backend.infra.redis.event.StartQuizRedisEvent
import wedding.kanshasai.v1.ParticipantEventType
import wedding.kanshasai.v1.StreamParticipantEventResponse

fun StreamParticipantEventResponse.Builder.setPreQuizEvent(event: PreQuizRedisEvent): StreamParticipantEventResponse.Builder = apply {
    participantEventType = ParticipantEventType.PARTICIPANT_EVENT_TYPE_PRE_QUIZ
    preQuizEventBody = this.preQuizEventBodyBuilder
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoices(
            event.choiceList.map { choice ->
                StreamParticipantEventResponse.PreQuizEvent.Choice.newBuilder()
                    .setChoiceId(choice.choiceId)
                    .setBody(choice.body)
                    .build()
            },
        )
        .build()
}

fun StreamParticipantEventResponse.Builder.setStartQuizEvent(event: StartQuizRedisEvent): StreamParticipantEventResponse.Builder = apply {
    participantEventType = ParticipantEventType.PARTICIPANT_EVENT_TYPE_QUIZ_START
    quizStartEventBody = this.quizStartEventBodyBuilder
        .setQuizId(event.quizId)
        .setBody(event.quizBody)
        .setQuizType(event.quizType)
        .addAllChoices(
            event.choiceList.map { choice ->
                StreamParticipantEventResponse.QuizStartEvent.Choice.newBuilder()
                    .setChoiceId(choice.choiceId)
                    .setBody(choice.body)
                    .build()
            },
        )
        .build()
}
