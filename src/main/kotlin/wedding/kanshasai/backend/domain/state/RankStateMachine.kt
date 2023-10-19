package wedding.kanshasai.backend.domain.state

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException

class RankStateMachine private constructor(val value: Int) {
    fun next() = runCatching {
        when {
            value == 1 -> throw InvalidStateTransitionException("Current state is last state")
            value <= 8 -> RankStateMachine(value - 1)
            value <= 11 -> RankStateMachine(8)
            else -> {
                // 12位以上
                val rankText = value.toString().toCharArray()

                if(rankText.last() == '1') {
                    // 下1桁が1の場合は10下げる
                    RankStateMachine(value - 10)
                } else if (rankText.last() == '0') {
                    // 下1桁が0の場合は9下げる
                    RankStateMachine(value - 9)
                } else {
                    rankText[rankText.lastIndex] = '1'
                    RankStateMachine(String(rankText).toInt())
                }
            }
        }
    }

    companion object {
        private const val MIN_VALUE = 1

        fun of(value: Int): RankStateMachine {
            if (value < MIN_VALUE) {
                throw InvalidArgumentException("value must be greater than or equal to $MIN_VALUE.")
            }
            return RankStateMachine(value)
        }
    }
}
