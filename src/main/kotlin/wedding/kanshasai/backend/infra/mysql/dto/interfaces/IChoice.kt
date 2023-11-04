package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.sql.Timestamp

interface IChoice {
    var choiceIdentifier: StandardIdentifier
    var quizId: ByteArray
    var body: String
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
