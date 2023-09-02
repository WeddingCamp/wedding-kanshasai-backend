package wedding.kanshasai.backend.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.connection.SubscriptionListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.redis.event.RedisEvent
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

@Service
class RedisEventService(
    private val redisTemplate: RedisTemplate<String, RedisEvent>,
    private val redisListenerContainer: ReactiveRedisMessageListenerContainer,
    private val objectMapper: ObjectMapper,
) {
    fun <T : RedisEvent> subscribe(eventType: KClass<out T>, sessionId: UlidId): Flow<T> = callbackFlow {
        logger.debug { "Subscribe message: ${eventType.simpleName}" }
        val subscription = redisListenerContainer.receive(ChannelTopic("$sessionId-${eventType.simpleName}")).subscribe { event ->
            Jackson2JsonRedisSerializer(objectMapper, eventType.java).deserialize(event.message.toByteArray())?.let {
                logger.debug { "Received message(${event.channel}): $it" }
                trySend(it)
            }
        }
        awaitClose {
            subscription.dispose()
        }
    }

    fun publish(event: RedisEvent, sessionId: UlidId) {
        logger.debug { "Publish message: $event" }
        redisTemplate.convertAndSend("$sessionId-${event::class.simpleName}", event)
    }
}
