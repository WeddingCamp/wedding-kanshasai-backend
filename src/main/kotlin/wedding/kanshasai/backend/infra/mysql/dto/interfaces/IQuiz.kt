package wedding.kanshasai.backend.infra.mysql.dto.interfaces

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import java.sql.Timestamp

interface IQuiz {
    val quizIdentifier: StandardIdentifier
    var eventId: ByteArray
    var body: String
    var correctAnswer: String
    var type: Int
    var isDeleted: Boolean
    val createdAt: Timestamp
    val updatedAt: Timestamp
}
