package wedding.kanshasai.backend.domain.manager

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import wedding.kanshasai.backend.domain.state.RankStateMachine
import wedding.kanshasai.backend.domain.state.ResultRankStateMachine
import wedding.kanshasai.backend.domain.state.ResultStateMachine
import wedding.kanshasai.backend.domain.value.ResultRankState
import wedding.kanshasai.backend.domain.value.ResultState

class ResultStateManagerTest {
    @Test
    fun next_shouldMakeStateTransitionInCorrectOrder() {
        expectedNextResultStateManagerParams.dropLast(1).forEachIndexed { index, params ->
            val manager = ResultStateManager.of(
                ResultStateMachine.of(params.first),
                ResultRankStateMachine.of(params.second, params.first.rankStateList),
                RankStateMachine.of(params.third),
            )
            val expectedNextParams = expectedNextResultStateManagerParams[index + 1]
            val expectedNextManager = ResultStateManager.of(
                ResultStateMachine.of(expectedNextParams.first),
                ResultRankStateMachine.of(expectedNextParams.second, expectedNextParams.first.rankStateList),
                RankStateMachine.of(expectedNextParams.third),
            )

            val nextManager = assertDoesNotThrow { manager.next().getOrThrow() }
            assertEquals(expectedNextManager, nextManager)
        }
    }

    companion object {
        private val expectedNextResultStateManagerParams = listOf(
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 100),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 91),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 81),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 71),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 61),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 51),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 41),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 31),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 21),
            Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 11),
            Triple(ResultState.RANKING_TOP_8, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_7, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_7, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_6, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_TOP_6, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_6, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_5, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_TOP_5, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_5, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_4, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_TOP_4, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_4, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_4_7_PRESENT, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE_MESSAGE, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE_MESSAGE, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_3, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_TOP_3, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_TOP_3, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_3, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_2, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_TOP_2, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_TOP_2, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_2, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_TOP_1, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_TOP_1, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_TOP_1, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_TOP_1, ResultRankState.PRESENT, 8),
        )
    }
}
