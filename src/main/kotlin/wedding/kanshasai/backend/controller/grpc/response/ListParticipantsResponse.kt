package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.domain.entity.Participant
import wedding.kanshasai.v1.ListParticipantsResponse

fun ListParticipantsResponse.Participant.Builder.setParticipant(participant: Participant):
    ListParticipantsResponse.Participant.Builder = apply {
    name = participant.name
    participantId = participant.id.toString()
    sessionId = participant.sessionId.toString()
    imageId = participant.imageId.toString()
    participantType = participant.type.toGrpcType()
}
