package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.v1.ListSessionsResponse

fun ListSessionsResponse.Session.Builder.setSession(session: Session): ListSessionsResponse.Session.Builder = apply {
    name = session.name
    sessionId = session.id.toString()
    eventId = session.eventId.toString()
}
