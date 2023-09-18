package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.v1.EventType

interface RedisEvent {
    val eventType: EventType
}
