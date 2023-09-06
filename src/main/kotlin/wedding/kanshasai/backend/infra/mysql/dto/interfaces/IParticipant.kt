package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier

interface IParticipant {
    var participantIdentifier: StandardIdentifier
    var sessionId: ByteArray
    var name: String
    var imageId: ByteArray?
    var type: Int
    var isDeleted: Boolean
}
