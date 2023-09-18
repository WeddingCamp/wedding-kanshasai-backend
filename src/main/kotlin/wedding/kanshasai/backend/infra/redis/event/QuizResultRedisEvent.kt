package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithResultRedisEntity
import wedding.kanshasai.v1.EventType
import wedding.kanshasai.v1.QuizType

@NoArg
data class QuizResultRedisEvent(
    override var quizId: String,
    override var quizBody: String,
    override var quizType: QuizType,
    override var choiceList: List<QuizChoiceWithResultRedisEntity>,
) : AbstractQuizRedisEvent<QuizChoiceWithResultRedisEntity>(quizId, quizBody, quizType, choiceList) {
    override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_RESULT
}
