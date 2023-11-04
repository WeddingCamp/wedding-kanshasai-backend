package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class QuizChoiceRedisEntity(
    override var choiceId: String,
    override var body: String,
) : AbstractQuizChoiceRedisEntity(choiceId, body)
