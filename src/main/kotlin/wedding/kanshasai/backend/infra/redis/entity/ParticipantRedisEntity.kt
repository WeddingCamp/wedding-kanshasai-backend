package wedding.kanshasai.backend.infra.redis.entity

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.v1.ParticipantType

@NoArg
data class ParticipantRedisEntity(
    var participantId: String,
    var name: String,
    var imageUrl: String,
    var participantType: ParticipantType,
    var connected: Boolean,
    var isAnswered: Boolean = false
)
