package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.value.IntroductionType

@NoArg
data class IntroductionRedisEvent(
    var introductionType: IntroductionType,
) : RedisEvent
