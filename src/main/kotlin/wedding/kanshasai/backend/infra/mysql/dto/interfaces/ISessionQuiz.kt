package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.SessionQuizIdentifier
import java.sql.Timestamp

interface ISessionQuiz {
    var sessionQuizIdentifier: SessionQuizIdentifier
    var isCompleted: Boolean
    var startedAt: Timestamp?
    var isDeleted: Boolean
    var createdAt: Timestamp
    var updatedAt: Timestamp
}
