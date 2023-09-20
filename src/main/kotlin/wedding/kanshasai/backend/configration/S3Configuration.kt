package wedding.kanshasai.backend.configration

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.Bucket
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration {
    @Value("\${cloud.aws.region.static}")
    private val s3ClientRegion: String? = null

    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String? = null

    @Value("\${cloud.aws.s3.endpoint}")
    private val endpoint: String? = null

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null

    @Bean
    fun s3Bucket(): Bucket {
        return Bucket(bucket)
    }

    @Bean
    fun s3Client(): AmazonS3 {
        val credentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withPathStyleAccessEnabled(true)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, s3ClientRegion))
            .build()
    }
}
