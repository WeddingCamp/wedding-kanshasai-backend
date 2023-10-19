package wedding.kanshasai.backend.domain.state

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException
import java.util.stream.Stream

class RankStateMachineTest {
    @ParameterizedTest
    @MethodSource("rankProvider")
    fun constructor_shouldCreateInstanceByPositiveNumber(from: Int, to: Int) {
        assertDoesNotThrow {
            RankStateMachine.of(from)
        }
    }

    @Test
    fun constructor_shouldFailedToCreateInstanceByZero() {
        assertThrows<InvalidArgumentException> {
            RankStateMachine.of(0)
        }
    }

    @ParameterizedTest
    @MethodSource("rankProvider")
    fun constructor_shouldFailedToCreateInstanceByNegativeNumber(from: Int, to: Int) {
        assertThrows<InvalidArgumentException> {
            RankStateMachine.of(-from)
        }
    }

    @ParameterizedTest
    @MethodSource("rankProvider")
    fun next_shouldMakeStateTransitionCorrect(from: Int, to: Int) {
        assertDoesNotThrow {
            val rank = RankStateMachine.of(from).next().getOrThrow()
            assertEquals(to, rank.value)
        }
    }

    @ParameterizedTest
    @MethodSource("rankProvider")
    fun next_shouldFailedToStateTransitionWhenOnePassed(from: Int, to: Int) {
        assertThrows<InvalidStateTransitionException> {
            RankStateMachine.of(1).next().getOrThrow()
        }
    }

    companion object {
        @JvmStatic
        private fun rankProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(106, 101),
                Arguments.arguments(101, 91),
                Arguments.arguments(91, 81),
                Arguments.arguments(81, 71),
                Arguments.arguments(71, 61),
                Arguments.arguments(70, 61),
                Arguments.arguments(69, 61),
                Arguments.arguments(68, 61),
                Arguments.arguments(67, 61),
                Arguments.arguments(66, 61),
                Arguments.arguments(65, 61),
                Arguments.arguments(64, 61),
                Arguments.arguments(63, 61),
                Arguments.arguments(62, 61),
                Arguments.arguments(61, 51),
                Arguments.arguments(60, 51),
                Arguments.arguments(59, 51),
                Arguments.arguments(58, 51),
                Arguments.arguments(57, 51),
                Arguments.arguments(56, 51),
                Arguments.arguments(55, 51),
                Arguments.arguments(54, 51),
                Arguments.arguments(53, 51),
                Arguments.arguments(52, 51),
                Arguments.arguments(51, 41),
                Arguments.arguments(41, 31),
                Arguments.arguments(31, 21),
                Arguments.arguments(21, 11),
                Arguments.arguments(20, 11),
                Arguments.arguments(19, 11),
                Arguments.arguments(18, 11),
                Arguments.arguments(17, 11),
                Arguments.arguments(16, 11),
                Arguments.arguments(15, 11),
                Arguments.arguments(14, 11),
                Arguments.arguments(13, 11),
                Arguments.arguments(12, 11),
                Arguments.arguments(11, 8),
                Arguments.arguments(10, 8),
                Arguments.arguments(9, 8),
                Arguments.arguments(8, 7),
                Arguments.arguments(7, 6),
                Arguments.arguments(6, 5),
                Arguments.arguments(5, 4),
                Arguments.arguments(4, 3),
                Arguments.arguments(3, 2),
                Arguments.arguments(2, 1)
            )
        }
    }
}