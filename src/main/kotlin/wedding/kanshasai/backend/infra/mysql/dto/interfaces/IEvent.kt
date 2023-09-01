package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.sql.Timestamp

interface IEvent {
    var eventIdentifier: StandardIdentifier
    var name: String
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
