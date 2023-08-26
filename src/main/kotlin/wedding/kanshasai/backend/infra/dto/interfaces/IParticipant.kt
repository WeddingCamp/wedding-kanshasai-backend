package wedding.kanshasai.backend.infra.dto.interfaces

import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

interface IParticipant {
    var participantIdentifier: StandardIdentifier
    var sessionId: ByteArray
    var name: String
    var imageId: ByteArray?
    var isDeleted: Boolean
}
