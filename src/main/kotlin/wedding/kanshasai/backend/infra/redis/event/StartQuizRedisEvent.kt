package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.v1.QuizType

@NoArg
data class StartQuizRedisEvent(
    override var quizId: String,
    override var quizBody: String,
    override var quizType: QuizType,
    override var choiceList: List<QuizChoiceRedisEvent>,
) : QuizRedisEvent(quizId, quizBody, quizType, choiceList)
