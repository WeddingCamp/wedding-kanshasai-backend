package wedding.kanshasai.backend.service

import io.github.g0dkar.qrcode.QRCode
import io.github.g0dkar.qrcode.QRCode.Companion.DEFAULT_CELL_SIZE
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.Session
import wedding.kanshasai.backend.domain.value.UlidId

@Service
class QrCodeService(
    private val s3Service: S3Service
) {
    @Value("\${frontend.url}")
    private val frontendUrl: String? = null

    fun generateQrCodeUrl(session: Session): String {
        if(s3Service.isFileExists(session.id)) return s3Service.generatePresignedUrl(session.id)
        QRCode("$frontendUrl/?s=${session.id}")
            .render(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE)
            .getBytes()
            .inputStream()
            .use {
                s3Service.uploadByteArrayInputStream(session.id, it)
            }
        return s3Service.generatePresignedUrl(session.id)
    }
}