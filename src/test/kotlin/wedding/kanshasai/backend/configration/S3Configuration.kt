package wedding.kanshasai.backend.configration

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration {
    @Bean
    fun s3Bucket(): Bucket {
        return Mockito.mock(Bucket::class.java)
    }

    @Bean
    fun s3Client(): AmazonS3 {
        return Mockito.mock(AmazonS3::class.java)
    }
}
