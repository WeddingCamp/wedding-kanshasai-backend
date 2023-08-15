package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.EventServiceGrpcKt.EventServiceCoroutineImplBase

@GrpcService
class EventController : EventServiceCoroutineImplBase() {
    override suspend fun createEvent(request: CreateEventRequest): CreateEventResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun updateEvent(request: UpdateEventRequest): UpdateEventResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun deleteEvent(request: DeleteEventRequest): DeleteEventResponse {
        TODO("NOT IMPLEMENTED")
    }

    override suspend fun listEvents(request: ListEventsRequest): ListEventsResponse {
        TODO("NOT IMPLEMENTED")
    }
}
