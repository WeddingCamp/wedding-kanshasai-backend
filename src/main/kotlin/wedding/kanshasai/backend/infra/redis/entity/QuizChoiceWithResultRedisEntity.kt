package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class QuizChoiceWithResultRedisEntity(
    override var choiceId: String,
    override var body: String,
    var count: Int,
    val correctChoice: Boolean,
) : AbstractQuizChoiceRedisEntity(choiceId, body)
