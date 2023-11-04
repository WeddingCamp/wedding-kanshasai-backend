package wedding.kanshasai.backend.domain.state

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException
import wedding.kanshasai.backend.domain.value.ResultState

class ResultStateMachineTest {
    private val expectResultStateOrderList = ResultState.values()

    @Test
    fun constructor_shouldCreateInstanceByInt() {
        expectResultStateOrderList.forEach {
            println(it)
            val fromInt = assertDoesNotThrow {
                ResultStateMachine.of(it.number)
            }
            val fromEnum = assertDoesNotThrow {
                ResultStateMachine.of(it)
            }
            assertEquals(fromInt, fromEnum)
        }
    }

    @Test
    fun next_shouldMakeStateTransitionInCorrectOrder() {
        var resultStateMachine = ResultStateMachine.of(expectResultStateOrderList.first())
        expectResultStateOrderList.drop(1).forEach {
            assertDoesNotThrow {
                resultStateMachine = resultStateMachine.next().getOrThrow()
            }
            assertEquals(it, resultStateMachine.value)
        }
        assertThrows<InvalidStateTransitionException> {
            resultStateMachine.next().getOrThrow()
        }
    }

    @Test
    fun back_shouldMakeStateTransitionInCorrectOrder() {
        var resultStateMachine = ResultStateMachine.of(expectResultStateOrderList.last())
        expectResultStateOrderList.reversed().drop(1).forEach {
            assertDoesNotThrow {
                resultStateMachine = resultStateMachine.back().getOrThrow()
            }
            assertEquals(it, resultStateMachine.value)
        }
        assertThrows<InvalidStateTransitionException> {
            resultStateMachine.back().getOrThrow()
        }
    }
}
