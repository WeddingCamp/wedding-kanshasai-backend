package wedding.kanshasai.backend.domain.manager

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import wedding.kanshasai.backend.domain.state.RankStateMachine
import wedding.kanshasai.backend.domain.state.ResultRankStateMachine
import wedding.kanshasai.backend.domain.state.ResultStateMachine
import wedding.kanshasai.backend.domain.value.ResultRankState
import wedding.kanshasai.backend.domain.value.ResultState
import java.util.stream.Stream

class ResultStateManagerTest {
    @ParameterizedTest
    @MethodSource("nextResultStateManagerProvider")
    fun next_shouldMakeStateTransitionInCorrectOrder(
        from: Triple<ResultState, ResultRankState, Int>,
        to: Triple<ResultState, ResultRankState, Int>,
    ) {
        val manager = from.toResultStateManager()
        val expectedNextManager = to.toResultStateManager()

        val nextManager = assertDoesNotThrow { manager.next().getOrThrow() }
        assertEquals(expectedNextManager, nextManager)
    }

    @ParameterizedTest
    @MethodSource("backResultStateManagerProvider")
    fun back_shouldMakeStateTransitionInCorrectOrder(
        from: Triple<ResultState, ResultRankState, Int>,
        to: Triple<ResultState, ResultRankState, Int>,
    ) {
        val manager = from.toResultStateManager()
        val expectedBackManager = to.toResultStateManager()

        val backManager = assertDoesNotThrow { manager.back().getOrThrow() }
        assertEquals(expectedBackManager, backManager)
    }

    private fun Triple<ResultState, ResultRankState, Int>.toResultStateManager() = ResultStateManager.of(
        ResultStateMachine.of(this.first),
        ResultRankStateMachine.of(this.second, this.first.rankStateList),
        RankStateMachine.of(this.third),
    )

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
            Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE_MESSAGE, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_JUST, ResultRankState.PRESENT, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE_MESSAGE, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.TITLE, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.PRE_RANK, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.RANK, 8),
            Triple(ResultState.RANKING_BOOBY, ResultRankState.PRESENT, 8),
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

        @JvmStatic
        private fun nextResultStateManagerProvider(): Stream<Arguments> {
            return expectedNextResultStateManagerParams.dropLast(1).mapIndexed { index, triple ->
                Arguments.arguments(
                    triple,
                    expectedNextResultStateManagerParams[index + 1],
                )
            }.stream()
        }

        @JvmStatic
        private fun backResultStateManagerProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 43),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 53),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 41),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 51),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 31),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 41),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 21),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 31),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 11),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 21),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 10),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 11),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 9),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 11),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_8, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_NORMAL, ResultRankState.RANK, 11),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_7, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_8, ResultRankState.RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_7, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_7, ResultRankState.RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_6, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_TOP_7, ResultRankState.RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_6, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_6, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_6, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_6, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_5, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_TOP_6, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_5, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_5, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_5, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_5, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_4, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_TOP_5, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_4, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_4, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_4, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_4, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_4_7_PRESENT, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_4, ResultRankState.PRE_RANK, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                    Triple(ResultState.RANKING_TOP_4_7_PRESENT, ResultRankState.PRESENT, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE_MESSAGE, 8),
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_JUST, ResultRankState.TITLE, 8),
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_JUST, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_JUST, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_JUST, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                    Triple(ResultState.RANKING_JUST, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE_MESSAGE, 8),
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.TITLE, 8),
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.TITLE, 8),
                    Triple(ResultState.RANKING_BOOBY, ResultRankState.DUMMY_TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.TITLE, 8),
                    Triple(ResultState.RANKING_TOP_3, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.TITLE, 8),
                    Triple(ResultState.RANKING_TOP_2, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.PRE_RANK, 8),
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.RANK, 8),
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.TITLE, 8),
                ),
                Arguments.arguments(
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.PRESENT, 8),
                    Triple(ResultState.RANKING_TOP_1, ResultRankState.TITLE, 8),
                ),
            )
        }
    }
}
