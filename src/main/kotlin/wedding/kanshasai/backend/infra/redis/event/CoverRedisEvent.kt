package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.v1.EventType

@NoArg
data class CoverRedisEvent(
    var isVisible: Boolean,
) : RedisEvent {
    override val eventType: EventType = EventType.EVENT_TYPE_COVER
}
