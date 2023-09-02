package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.sql.Timestamp

interface ISession {
    var sessionIdentifier: StandardIdentifier
    var eventId: ByteArray
    var name: String
    var stateId: Int
    var currentIntroductionId: Int?
    var currentQuizId: ByteArray?
    var isCoverVisible: Boolean
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
