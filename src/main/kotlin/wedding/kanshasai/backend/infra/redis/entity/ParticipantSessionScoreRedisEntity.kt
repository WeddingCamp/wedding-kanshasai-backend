package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class ParticipantSessionScoreRedisEntity(
    var participantName: String,
    var score: Int,
    var isEmphasis: Boolean,
    var rank: Int,
    var time: Float,
)
