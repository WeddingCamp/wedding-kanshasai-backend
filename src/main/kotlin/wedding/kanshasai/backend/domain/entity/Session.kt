package wedding.kanshasai.backend.domain.entity

import wedding.kanshasai.backend.domain.value.UlidId
import java.sql.Timestamp

data class Session(
    val id: UlidId,
    val event: Event,
    var name: String,
    var sessionStateId: Int,
    var coverScreenId: Int?,
    var currentQuizId: ByteArray?, // TODO: Entityに置き換える
    var isDeleted: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
)
