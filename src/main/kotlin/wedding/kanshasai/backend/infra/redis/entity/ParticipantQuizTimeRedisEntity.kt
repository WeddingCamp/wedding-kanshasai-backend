package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg

@NoArg
data class ParticipantQuizTimeRedisEntity(
    var participantName: String,
    var participantImageId: String,
    var time: Float,
)