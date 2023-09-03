package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.v1.CreateSessionResponse

fun CreateSessionResponse.Builder.setSession(session: Session): CreateSessionResponse.Builder = apply {
    name = session.name
    sessionId = session.id.toString()
    eventId = session.eventId.toString()
}
