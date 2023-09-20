package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceRedisEntity
import wedding.kanshasai.v1.EventType
import wedding.kanshasai.v1.QuizType

@NoArg
data class ShowQuizRedisEvent(
    override var quizId: String,
    override var quizBody: String,
    override var quizType: QuizType,
    override var choiceList: List<QuizChoiceRedisEntity>,
) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList) {
    override val eventType: EventType = EventType.EVENT_TYPE_SHOW_QUIZ
}
