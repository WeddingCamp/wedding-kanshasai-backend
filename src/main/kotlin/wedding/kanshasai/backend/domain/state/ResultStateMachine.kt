package wedding.kanshasai.backend.domain.state

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException
import wedding.kanshasai.backend.domain.value.ResultState

/**
 * ResultStateを管理するステートマシン
 */
class ResultStateMachine private constructor(override val value: ResultState) :
    StateMachine<ResultStateMachine, ResultState>(value) {

        fun next() = runCatching {
            val nextState = value.nextState
            if (nextState == null) {
                throw InvalidStateTransitionException("Current state is the last state")
            }
            ResultStateMachine(nextState)
        }

        fun back() = runCatching {
            val backState = value.backState
            if (backState == null) {
                throw InvalidStateTransitionException("Current state is the first state")
            }
            ResultStateMachine(backState)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is ResultStateMachine) return false
            if (value != other.value) return false
            return true
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return value.name
        }

    companion object {
            fun of(value: Int): ResultStateMachine {
                ResultState.values().forEach {
                    if (it.number == value) return ResultStateMachine(it)
                }
                throw InvalidArgumentException("Invalid ResultState value. (value=$value)")
            }

            fun of(value: ResultState): ResultStateMachine {
                return ResultStateMachine(value)
            }
        }
    }
