package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.value.IntroductionType
import wedding.kanshasai.v1.EventType

@NoArg
data class IntroductionRedisEvent(
    var introductionType: IntroductionType,
) : RedisEvent {
    override val eventType: EventType = EventType.EVENT_TYPE_INTRODUCTION
}
