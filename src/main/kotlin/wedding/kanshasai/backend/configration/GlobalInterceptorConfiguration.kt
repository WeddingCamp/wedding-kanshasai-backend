package wedding.kanshasai.backend.configration

import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.springframework.context.annotation.Configuration
import wedding.kanshasai.backend.configration.interceptor.GrpcLogInterceptor

@Configuration(proxyBeanMethods = false)
class GlobalInterceptorConfiguration {
    @GrpcGlobalServerInterceptor
    fun logServerInterceptor() = GrpcLogInterceptor()
}