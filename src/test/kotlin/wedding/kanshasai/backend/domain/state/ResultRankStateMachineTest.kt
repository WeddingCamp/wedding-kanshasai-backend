package wedding.kanshasai.backend.domain.state

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException
import wedding.kanshasai.backend.domain.value.ResultRankState

class ResultRankStateMachineTest {
    private val expectResultRankStateOrderList = ResultRankState.values().toList()

    @Test
    fun constructor_shouldCreateInstanceByInt() {
        expectResultRankStateOrderList.forEach {
            val fromInt = assertDoesNotThrow {
                ResultRankStateMachine.of(it.number, expectResultRankStateOrderList)
            }
            val fromEnum = assertDoesNotThrow {
                ResultRankStateMachine.of(it, expectResultRankStateOrderList)
            }
            assertEquals(fromInt, fromEnum)
        }
    }

    @Test
    fun constructor_shouldThrowExceptionWhenRankStateListIsEmpty() {
        assertThrows<InvalidStateException> {
            ResultRankStateMachine.of(ResultRankState.RANK, emptyList())
        }
    }

    @Test
    fun next_shouldMakeStateTransitionInCorrectOrder() {
        var resultRankStateMachine = ResultRankStateMachine.of(expectResultRankStateOrderList.first(), expectResultRankStateOrderList)
        expectResultRankStateOrderList.drop(1).forEach {
            assertDoesNotThrow {
                resultRankStateMachine = resultRankStateMachine.next().getOrThrow()
            }
            assertEquals(it, resultRankStateMachine.value)
        }
        assertThrows<InvalidStateTransitionException> {
            resultRankStateMachine.next().getOrThrow()
        }
    }

    @Test
    fun back_shouldMakeStateTransitionInCorrectOrder() {
        var resultRankStateMachine = ResultRankStateMachine.of(expectResultRankStateOrderList.last(), expectResultRankStateOrderList)
        expectResultRankStateOrderList.reversed().drop(1).forEach {
            assertDoesNotThrow {
                resultRankStateMachine = resultRankStateMachine.back().getOrThrow()
            }
            assertEquals(it, resultRankStateMachine.value)
        }
        assertThrows<InvalidStateTransitionException> {
            resultRankStateMachine.back().getOrThrow()
        }
    }
}