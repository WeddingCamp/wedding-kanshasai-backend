package wedding.kanshasai.backend

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import wedding.kanshasai.backend.configration.RedisTestConfiguration

@SpringBootTest(
    "grpc.server.inProcessName=test",
    "grpc.server.port=-1",
    "grpc.client.inProcess.address=in-process:test",
    classes = [RedisTestConfiguration::class],
)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
annotation class WeddingKanshasaiSpringBootTest

fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
