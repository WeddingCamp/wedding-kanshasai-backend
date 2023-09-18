package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.ParticipantQuizTimeRedisEntity
import wedding.kanshasai.v1.EventType

@NoArg
data class QuizSpeedRankingRedisEvent(
    var participantQuizTimeList: List<ParticipantQuizTimeRedisEntity>,
) : RedisEvent {
    override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_SPEED_RANKING
}
