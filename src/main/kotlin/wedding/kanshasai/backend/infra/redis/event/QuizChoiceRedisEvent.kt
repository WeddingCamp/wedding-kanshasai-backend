package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class QuizChoiceRedisEvent(
    var choiceId: String,
    var body: String,
)
