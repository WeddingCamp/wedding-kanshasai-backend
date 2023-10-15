package wedding.kanshasai.backend.domain.state

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException

class ResultState private constructor(override val value: ResultStateEnum) :
    StateMachine<ResultState, ResultState.ResultStateEnum>(value) {
        enum class ResultStateEnum(
            override val number: Int,
        ) : StateMachineEnumInterface {
            RANKING_NORMAL(1) {
                override val nextStateList
                    get() = listOf(RANKING_TOP_8)
            },
            RANKING_TOP_8(10) {
                override val nextStateList
                    get() = listOf(RANKING_TOP_4_7)
            },
            RANKING_TOP_4_7(20) {
                override val nextStateList
                    get() = listOf(RANKING_TOP_4_7_PRESENT)
            },
            RANKING_TOP_4_7_PRESENT(30) {
                override val nextStateList
                    get() = listOf(RANKING_BOOBY)
            },
            RANKING_BOOBY(40) {
                override val nextStateList
                    get() = listOf(RANKING_JUST)
            },
            RANKING_JUST(50) {
                override val nextStateList
                    get() = listOf(RANKING_TOP_1_3)
            },
            RANKING_TOP_1_3(60) {
                override val nextStateList
                    get() = listOf<ResultStateEnum>()
            },
        }

        companion object {
            fun of(value: Int): ResultState {
                ResultStateEnum.values().forEach {
                    if (it.number == value) return ResultState(it)
                }
                throw InvalidArgumentException("Invalid ResultState value. (value=$value)")
            }

            val RANKING_NORMAL = ResultState(ResultStateEnum.RANKING_NORMAL)
            val RANKING_TOP_8 = ResultState(ResultStateEnum.RANKING_TOP_8)
            val RANKING_TOP_4_7 = ResultState(ResultStateEnum.RANKING_TOP_4_7)
            val RANKING_TOP_4_7_PRESENT = ResultState(ResultStateEnum.RANKING_TOP_4_7_PRESENT)
            val RANKING_BOOBY = ResultState(ResultStateEnum.RANKING_BOOBY)
            val RANKING_JUST = ResultState(ResultStateEnum.RANKING_JUST)
            val RANKING_TOP_1_3 = ResultState(ResultStateEnum.RANKING_TOP_1_3)

            val values = ResultStateEnum.values().map { ResultState(it) }
        }
    }
