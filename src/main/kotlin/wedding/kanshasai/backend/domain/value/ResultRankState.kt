package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.domain.state.StateInterface

enum class ResultRankState(
    override val number: Int,
) : StateInterface {
    RANK(1) {
        override val nextStateList get() = listOf(PRESENT)
    },
    PRESENT(10) {
        override val nextStateList get() = listOf(PRE_RANK, PRESENT, DUMMY_TITLE, TITLE)
    },
    PRE_RANK(20) {
        override val nextStateList get() = listOf(RANK)
    },
    DUMMY_TITLE(30) {
        override val nextStateList get() = listOf(DUMMY_TITLE_MESSAGE)
    },
    DUMMY_TITLE_MESSAGE(40) {
        override val nextStateList get() = listOf(TITLE)
    },
    TITLE(50) {
        override val nextStateList get() = listOf(PRE_RANK)
    },
}
