package wedding.kanshasai.backend;

import jakarta.annotation.PreDestroy
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer


@TestConfiguration
@EnableConfigurationProperties(RedisProperties::class)
class RedisTestAutoConfiguration {

    @PreDestroy
    fun preDestroy() {
        // サーバを停止
        stop()
    }

    companion object {
        private var redisServer: RedisServer? = null
        @Synchronized
        fun start(redisProperties: RedisProperties) {
            if (redisServer == null) {
                redisServer = RedisServer(redisProperties.port)
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