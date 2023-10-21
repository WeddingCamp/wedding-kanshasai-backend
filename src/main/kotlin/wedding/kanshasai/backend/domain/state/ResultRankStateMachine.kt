package wedding.kanshasai.backend.domain.state

import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException
import wedding.kanshasai.backend.domain.value.ResultRankState

/**
 * ResultRankStateを管理するステートマシン
 */
class ResultRankStateMachine private constructor(
    override val value: ResultRankState,
    private val rankStateList: List<ResultRankState>,
) : StateMachine<ResultRankStateMachine, ResultRankState>(value) {
    fun next() = runCatching {
        val pos = rankStateList.indexOf(value)
        if (pos == rankStateList.size - 1) {
            throw InvalidStateTransitionException("Current state is the last state")
        }
        ResultRankStateMachine(rankStateList[pos + 1], rankStateList)
    }

    fun back() = runCatching {
        val pos = rankStateList.indexOf(value)
        if (pos == 0) {
            throw InvalidStateTransitionException("Current state is the first state")
        }
        ResultRankStateMachine(rankStateList[pos - 1], rankStateList)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ResultRankStateMachine) return false
        if (value != other.value) return false
        if (!rankStateList.toTypedArray().contentEquals(other.rankStateList.toTypedArray())) return false
        return true
    }

    companion object {
        fun of(value: Int, rankStateList: List<ResultRankState>): ResultRankStateMachine {
            if (rankStateList.isEmpty()) {
                throw InvalidStateException("rankStateList is empty.")
            }
            ResultRankState.values().forEach {
                if (it.number == value && rankStateList.contains(it)) {
                    return ResultRankStateMachine(it, rankStateList)
                }
            }
            throw InvalidStateException("Invalid ResultRankState value. (value=$value)")
        }

        fun of(value: ResultRankState, rankStateList: List<ResultRankState>): ResultRankStateMachine {
            if (rankStateList.isEmpty()) {
                throw InvalidStateException("rankStateList is empty.")
            }
            if (!rankStateList.contains(value)) {
                throw InvalidStateException("Invalid ResultRankState value. (value=$value)")
            }
            return ResultRankStateMachine(value, rankStateList)
        }

        fun of(rankStateList: List<ResultRankState>): ResultRankStateMachine {
            if (rankStateList.isEmpty()) {
                throw InvalidStateException("rankStateList is empty.")
            }
            return ResultRankStateMachine(rankStateList.first(), rankStateList)
        }
    }
}
