package wedding.kanshasai.backend.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.value.UlidId
import java.io.ByteArrayInputStream
import java.util.*

@Service
class S3Service(
    private val s3Client: AmazonS3,
    private val s3Bucket: Bucket,
) {
    fun generatePresignedUrl(id: String): String {
        val fileId = try {
            UlidId.of(id)
        } catch (e: Exception) {
            null
        }
        return generatePresignedUrl(fileId)
    }

    fun generatePresignedUrl(id: UlidId?): String {
        val fileId = id?.toString() ?: "PLACEHOLDER"
        val expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60)
        val url = s3Client.generatePresignedUrl(s3Bucket.name, fileId, expiration)
        return url.toString()
    }

    fun generatePresignedUrl(id: UlidId?, method: HttpMethod): String {
        val fileId = id?.toString() ?: "PLACEHOLDER"
        val expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60)
        val url = s3Client.generatePresignedUrl(s3Bucket.name, fileId, expiration, method)
        return url.toString()
    }

    fun uploadByteArrayInputStream(id: UlidId, byteArrayInputStream: ByteArrayInputStream) {
        s3Client.putObject(s3Bucket.name, id.toString(), byteArrayInputStream, null)
    }

    fun isFileExists(id: UlidId): Boolean {
        return s3Client.doesObjectExist(s3Bucket.name, id.toString())
    }
}
