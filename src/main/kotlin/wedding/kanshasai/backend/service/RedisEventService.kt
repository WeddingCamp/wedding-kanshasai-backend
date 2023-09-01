package wedding.kanshasai.backend.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.redis.type.RedisEvent
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

@Service
class RedisEventService(
    private val redisTemplate: RedisTemplate<String, RedisEvent>,
    private val redisListenerContainer: RedisMessageListenerContainer,
    private val objectMapper: ObjectMapper,
) {
    fun <T : RedisEvent> subscribe(eventType: KClass<out T>, sessionId: UlidId): Flow<T> = callbackFlow {
        val messageListener = MessageListener { message, pattern ->
            Jackson2JsonRedisSerializer(objectMapper, eventType.java).deserialize(message.body)?.let {
                logger.debug { "Received message($pattern): $it" }
                trySend(it)
            }
        }
        redisListenerContainer.addMessageListener(messageListener, PatternTopic("$sessionId-${eventType.simpleName}"))
        awaitClose {
            redisListenerContainer.removeMessageListener(messageListener)
        }
    }

    fun publish(event: RedisEvent, sessionId: UlidId) {
        redisTemplate.convertAndSend("$sessionId-${event::class.simpleName}", event)
    }
}
