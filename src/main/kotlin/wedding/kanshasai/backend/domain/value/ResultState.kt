package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.domain.state.StateInterface

enum class ResultState(
    override val number: Int,
) : StateInterface {
    RANKING_NORMAL(1) {
        override val nextStateList get() = listOf(RANKING_TOP_8)
        override val rankStateList get() = listOf(ResultRankState.RANK)
        override val nextState get() = RANKING_TOP_8
        override val backState get() = null
    },
    RANKING_TOP_8(10) {
        override val nextStateList get() = listOf(RANKING_TOP_7)
        override val rankStateList get() = listOf(ResultRankState.RANK)
        override val nextState get() = RANKING_TOP_7
        override val backState get() = RANKING_NORMAL
    },
    RANKING_TOP_7(20) {
        override val nextStateList get() = listOf(RANKING_TOP_4_6)
        override val rankStateList get() = listOf(ResultRankState.RANK, ResultRankState.PRESENT)
        override val nextState get() = RANKING_TOP_4_6
        override val backState get() = RANKING_TOP_8
    },
    RANKING_TOP_4_6(30) {
        override val nextStateList get() = listOf(RANKING_TOP_4_7_PRESENT)
        override val rankStateList
            get() = listOf(
                ResultRankState.PRE_RANK,
                ResultRankState.RANK,
                ResultRankState.PRESENT,
            )
        override val nextState get() = RANKING_TOP_4_7_PRESENT
        override val backState get() = RANKING_TOP_7
    },
    RANKING_TOP_4_7_PRESENT(40) {
        override val nextStateList get() = listOf(RANKING_BOOBY)
        override val rankStateList get() = listOf(ResultRankState.PRESENT)
        override val nextState get() = RANKING_BOOBY
        override val backState get() = RANKING_TOP_4_6
    },
    RANKING_BOOBY(50) {
        override val nextStateList get() = listOf(RANKING_JUST)
        override val rankStateList
            get() = listOf(
                ResultRankState.DUMMY_TITLE,
                ResultRankState.DUMMY_TITLE_MESSAGE,
                ResultRankState.TITLE,
                ResultRankState.PRE_RANK,
                ResultRankState.RANK,
                ResultRankState.PRESENT,
            )
        override val nextState get() = RANKING_JUST
        override val backState get() = RANKING_TOP_4_7_PRESENT
    },
    RANKING_JUST(60) {
        override val nextStateList get() = listOf(RANKING_TOP_1_3)
        override val rankStateList
            get() = listOf(
                ResultRankState.DUMMY_TITLE,
                ResultRankState.DUMMY_TITLE_MESSAGE,
                ResultRankState.TITLE,
                ResultRankState.PRE_RANK,
                ResultRankState.RANK,
                ResultRankState.PRESENT,
            )
        override val nextState get() = RANKING_TOP_1_3
        override val backState get() = RANKING_BOOBY
    },
    RANKING_TOP_1_3(70) {
        override val nextStateList get() = listOf<ResultState>()
        override val rankStateList
            get() = listOf(
                ResultRankState.TITLE,
                ResultRankState.PRE_RANK,
                ResultRankState.RANK,
                ResultRankState.PRESENT,
            )
        override val nextState get() = null
        override val backState get() = RANKING_JUST
    },
    ;

    abstract val rankStateList: List<ResultRankState>
    abstract val nextState: ResultState?
    abstract val backState: ResultState?

    companion object {
        fun of(value: Int): ResultState {
            ResultState.values().forEach {
                if (it.number == value) return it
            }
            throw IllegalArgumentException("Invalid ResultState value. (value=$value)")
        }
    }
}
