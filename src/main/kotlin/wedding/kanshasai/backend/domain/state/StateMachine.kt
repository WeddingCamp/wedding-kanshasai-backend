package wedding.kanshasai.backend.domain.state

import io.github.oshai.kotlinlogging.KotlinLogging
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException

private val logger = KotlinLogging.logger {}

abstract class StateMachine<S : StateMachineInterface<E>, E : StateMachineEnumInterface>
    (override val value: E) : StateMachineInterface<E> {
        override fun toString(): String = value.name
        fun toNumber(): Int = value.number

        fun next(nextState: S) = runCatching {
            if (this == nextState) {
                logger.warn { "Recursive state transition from '${value.name}' to '${nextState.value.name}' has occurred" }
            }
            if (!value.nextStateList.contains(nextState.value)) {
                throw InvalidStateTransitionException("Transition from '${value.name}' to '${nextState.value.name}' is invalid")
            }
            return@runCatching nextState
        }
    }

interface StateMachineInterface<T : StateMachineEnumInterface> {
    val value: T
}

interface StateMachineEnumInterface {
    val name: String
    val ordinal: Int
    val number: Int
    val nextStateList: List<StateMachineEnumInterface>
}
