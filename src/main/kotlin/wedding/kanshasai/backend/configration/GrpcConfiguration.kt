package wedding.kanshasai.backend.configration

import net.devh.boot.grpc.client.autoconfigure.*
import net.devh.boot.grpc.common.autoconfigure.*
import net.devh.boot.grpc.server.autoconfigure.*
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Configuration

@Configuration
@ImportAutoConfiguration(
    GrpcClientAutoConfiguration::class,
    GrpcClientMetricAutoConfiguration::class,
    GrpcClientHealthAutoConfiguration::class,
    GrpcClientSecurityAutoConfiguration::class,
    GrpcClientTraceAutoConfiguration::class,
    GrpcDiscoveryClientAutoConfiguration::class,
    GrpcCommonCodecAutoConfiguration::class,
    GrpcCommonTraceAutoConfiguration::class,
    GrpcAdviceAutoConfiguration::class,
    GrpcHealthServiceAutoConfiguration::class,
    GrpcMetadataConsulConfiguration::class,
    GrpcMetadataEurekaConfiguration::class,
    GrpcMetadataNacosConfiguration::class,
    GrpcMetadataZookeeperConfiguration::class,
    GrpcReflectionServiceAutoConfiguration::class,
    GrpcServerAutoConfiguration::class,
    GrpcServerFactoryAutoConfiguration::class,
    GrpcServerMetricAutoConfiguration::class,
    GrpcServerSecurityAutoConfiguration::class,
    GrpcServerTraceAutoConfiguration::class
)
class GrpcConfiguration
