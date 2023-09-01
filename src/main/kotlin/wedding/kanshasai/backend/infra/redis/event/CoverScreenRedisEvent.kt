package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.value.CoverScreenType

@NoArg
data class CoverScreenRedisEvent(
    var coverScreenType: CoverScreenType,
) : RedisEvent
