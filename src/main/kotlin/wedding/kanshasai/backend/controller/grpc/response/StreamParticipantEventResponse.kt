package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.infra.redis.event.NextQuizRedisEvent
import wedding.kanshasai.v1.ParticipantEventType
import wedding.kanshasai.v1.StreamParticipantEventResponse

fun StreamParticipantEventResponse.Builder.setNextQuizEvent(event: NextQuizRedisEvent): StreamParticipantEventResponse.Builder = apply {
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