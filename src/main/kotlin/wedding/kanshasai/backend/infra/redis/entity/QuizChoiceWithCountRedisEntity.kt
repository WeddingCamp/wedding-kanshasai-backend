package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class QuizChoiceWithCountRedisEntity(
    override var choiceId: String,
    override var body: String,
    var count: Int,
) : AbstractQuizChoiceRedisEntity(choiceId, body)
