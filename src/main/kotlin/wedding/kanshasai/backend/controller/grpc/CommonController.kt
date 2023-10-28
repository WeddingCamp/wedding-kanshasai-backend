package wedding.kanshasai.backend.controller.grpc

import com.amazonaws.HttpMethod
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.service.S3Service
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.CommonServiceGrpcKt.CommonServiceCoroutineImplBase

@GrpcService
class CommonController(
    private val s3Service: S3Service,
) : CommonServiceCoroutineImplBase() {
    override suspend fun generatePresignedUrl(request: GeneratePresignedUrlRequest): GeneratePresignedUrlResponse {
        val id = UlidId.new()
        val url = s3Service.generatePresignedUrl(id, HttpMethod.PUT)
        return GeneratePresignedUrlResponse.newBuilder()
            .setPresignedUrl(url)
            .setFileId(id.toString())
            .build()
    }
}
