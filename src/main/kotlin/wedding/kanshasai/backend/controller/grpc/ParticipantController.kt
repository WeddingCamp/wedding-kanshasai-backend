package wedding.kanshasai.backend.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.v1.ParticipantServiceGrpcKt.ParticipantServiceCoroutineImplBase

@GrpcService
class ParticipantController: ParticipantServiceCoroutineImplBase() {
}
