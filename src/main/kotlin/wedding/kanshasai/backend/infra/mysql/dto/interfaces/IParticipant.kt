package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.sql.Timestamp

interface IParticipant {
    var participantIdentifier: StandardIdentifier
    var sessionId: ByteArray
    var name: String
    var imageId: ByteArray?
    var type: Int
    var isConnected: Boolean
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
