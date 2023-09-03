package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.v1.QuizType

@NoArg
data class NextQuizRedisEvent(
    var quizId: String,
    var quizBody: String,
    var quizType: QuizType,
    var choiceList: List<NextQuizRedisEventChoice>,
) : RedisEvent

@NoArg
data class NextQuizRedisEventChoice(
    var choiceId: String,
    var body: String,
)
