package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
abstract class AbstractQuizChoiceRedisEntity(
    open var choiceId: String,
    open var body: String,
)
