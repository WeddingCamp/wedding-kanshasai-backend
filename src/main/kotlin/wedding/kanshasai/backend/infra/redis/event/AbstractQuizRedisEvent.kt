package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.AbstractQuizChoiceRedisEntity
import wedding.kanshasai.v1.QuizType

@NoArg
abstract class AbstractQuizRedisEvent<CHOICE : AbstractQuizChoiceRedisEntity>(
    open var quizId: String,
    open var quizBody: String,
    open var quizType: QuizType,
    open var choiceList: List<CHOICE>,
) : RedisEvent
