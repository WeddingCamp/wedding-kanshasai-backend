package wedding.kanshasai.backend.configration

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.ReadFrom
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import wedding.kanshasai.backend.infra.redis.type.RedisEvent

@Configuration
class RedisConfiguration(
    private val redisProperties: RedisProperties,
) {
    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.builder()
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()
        val serverConfig = RedisStandaloneConfiguration().also {
            it.hostName = redisProperties.host
            it.port = redisProperties.port
            it.database = redisProperties.database
        }
        return LettuceConnectionFactory(serverConfig, clientConfig)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, RedisEvent> {
        return RedisTemplate<String, RedisEvent>().also {
            it.connectionFactory = redisConnectionFactory()
            it.keySerializer = StringRedisSerializer()
            it.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper())
            it.afterPropertiesSet()
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }

    @Bean
    fun redisContainer(): RedisMessageListenerContainer {
        return RedisMessageListenerContainer().also {
            it.setConnectionFactory(redisConnectionFactory())
        }
    }
}
