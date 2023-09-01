package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.sql.Timestamp

interface ISession {
    var sessionIdentifier: StandardIdentifier
    var eventId: ByteArray
    var name: String
    var stateId: Int
    var coverScreenId: Int?
    var currentQuizId: ByteArray?
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
