package wedding.kanshasai.backend.infra.redis.type

import wedding.kanshasai.backend.configration.NoArg
import wedding.kanshasai.backend.domain.value.CoverScreenType

@NoArg
data class CoverScreenRedisEvent(
    var coverScreenType: CoverScreenType,
) : RedisEvent
