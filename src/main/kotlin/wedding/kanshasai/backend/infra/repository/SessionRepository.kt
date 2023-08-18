package wedding.kanshasai.backend.infra.repository

import org.springframework.stereotype.Repository
import wedding.kanshasai.backend.domain.value.UlidId

@Repository
class SessionRepository {
    fun findById(ulid: UlidId) {}
}
