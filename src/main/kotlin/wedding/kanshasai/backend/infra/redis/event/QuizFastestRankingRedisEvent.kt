package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.ParticipantQuizTimeRedisEntity

@NoArg
data class QuizFastestRankingRedisEvent(
    var participantQuizTimeList: List<ParticipantQuizTimeRedisEntity>,
) : RedisEvent
