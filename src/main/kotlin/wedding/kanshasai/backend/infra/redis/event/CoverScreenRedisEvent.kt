package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class CoverScreenRedisEvent(
    var isVisible: Boolean
) : RedisEvent
