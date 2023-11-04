package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.service.EventService
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.EventServiceGrpcKt.EventServiceCoroutineImplBase

@GrpcService
class EventController(
    private val eventService: EventService,
) : EventServiceCoroutineImplBase() {
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
        val eventList = eventService.listEventAll()
        val grpcEventList = eventList.map { p ->
            ListEventsResponse.Event.newBuilder().let {
                it.eventId = p.id.toString()
                it.eventName = p.name
                it.build()
            }
        }
        return ListEventsResponse.newBuilder().let {
            it.addAllEvents(grpcEventList)
            it.build()
        }
    }
}
