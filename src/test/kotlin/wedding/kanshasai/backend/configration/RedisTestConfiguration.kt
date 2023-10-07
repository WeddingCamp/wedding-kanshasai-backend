package wedding.kanshasai.backend.configration

import jakarta.annotation.PreDestroy
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
@EnableConfigurationProperties(RedisProperties::class)
class RedisTestConfiguration(redisProperties: RedisProperties) {
    init {
        start(redisProperties)
    }

    @PreDestroy
    fun preDestroy() {
        stop()
    }

    companion object {
        private var redisServer: RedisServer? = null

        @Synchronized
        fun start(redisProperties: RedisProperties) {
            if (redisServer == null) {
                redisServer = RedisServer.builder()
                    .port(redisProperties.port)
                    .setting("maxheap 128M")
                    .setting("daemonize no")
                    .setting("appendonly no")
                    .build()
                redisServer!!.start()
            }
        }

        @Synchronized
        fun stop() {
            if (redisServer != null) {
                redisServer!!.stop()
                redisServer = null
            }
        }
    }
}
