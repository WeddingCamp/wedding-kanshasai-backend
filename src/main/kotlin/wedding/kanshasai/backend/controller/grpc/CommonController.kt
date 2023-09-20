package wedding.kanshasai.backend.controller.grpc

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import net.devh.boot.grpc.server.service.GrpcService
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.v1.*
import wedding.kanshasai.v1.CommonServiceGrpcKt.CommonServiceCoroutineImplBase
import java.util.*

@GrpcService
class CommonController(
    private val s3Client: AmazonS3,
    private val s3Bucket: Bucket,
) : CommonServiceCoroutineImplBase() {
    override suspend fun generatePresignedUrl(request: GeneratePresignedUrlRequest): GeneratePresignedUrlResponse {
        val id = UlidId.new()
        val url = s3Client.generatePresignedUrl(s3Bucket.name, id.toString(), Date(System.currentTimeMillis() + 1000 * 60 * 60))
        return GeneratePresignedUrlResponse.newBuilder()
            .setPresignedUrl(url.toString())
            .setFileId(id.toString())
            .build()
    }
}
