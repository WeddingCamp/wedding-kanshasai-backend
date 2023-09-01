package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.ParticipantAnswerIdentifier
import java.sql.Timestamp

interface IParticipantAnswer {
    var participantAnswerIdentifier: ParticipantAnswerIdentifier
    var answer: String
    var time: Float?
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
