package wedding.kanshasai.backend.infra.mysql.dto

import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.dto.interfaces.ISession
import java.sql.Timestamp

data class SessionDto(
    override var sessionIdentifier: StandardIdentifier = StandardIdentifier(byteArrayOf()),
    override var eventId: ByteArray = byteArrayOf(),
    override var name: String = "",
    override var stateId: Int = 1,
    override var currentQuizId: ByteArray? = null,
    override var currentIntroductionId: Int? = null,
    override var isCoverVisible: Boolean = false,
    override var isDeleted: Boolean = false,
    override var createdAt: Timestamp = Timestamp(0),
    override var updatedAt: Timestamp = Timestamp(0),
) : ISession, IdentifiableDto<StandardIdentifier>(isDeleted) {
    override var identifier: StandardIdentifier
        get() = sessionIdentifier
        set(value) {
            sessionIdentifier = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionDto

        if (sessionIdentifier != other.sessionIdentifier) return false
        if (!eventId.contentEquals(other.eventId)) return false
        if (name != other.name) return false
        if (stateId != other.stateId) return false
        if (isCoverVisible != other.isCoverVisible) return false
        if (currentQuizId != null) {
            if (other.currentQuizId == null) return false
            if (!currentQuizId.contentEquals(other.currentQuizId)) return false
        } else if (other.currentQuizId != null) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode() = sessionIdentifier.hashCode()
}
