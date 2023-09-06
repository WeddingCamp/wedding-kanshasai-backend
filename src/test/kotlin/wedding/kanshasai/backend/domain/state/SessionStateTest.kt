package wedding.kanshasai.backend.domain.state

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException
import java.util.stream.Stream

class SessionStateTest {
    @ParameterizedTest
    @MethodSource("sessionStateProvider")
    fun next_shouldProperlyThrowException(
        sessionState: SessionState,
        validTransitionDestinationList: List<SessionState>,
    ) {
        SessionState.values.forEach {
            if (it == sessionState) {
                assertEquals(it.toNumber(), sessionState.toNumber())
                assertEquals(it.toString(), sessionState.toString())
            }

            if (it == sessionState || validTransitionDestinationList.contains(it)) {
                assertDoesNotThrow {
                    sessionState.next(it).getOrThrow()
                }
            } else {
                assertThrows<InvalidStateTransitionException> {
                    sessionState.next(it).getOrThrow()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        private fun sessionStateProvider(): Stream<Arguments> {
            return Stream.of(
                arguments(
                    SessionState.INTRODUCTION,
                    listOf(
                        SessionState.QUIZ_WAITING,
                    ),
                ),
                arguments(
                    SessionState.QUIZ_WAITING,
                    listOf(
                        SessionState.QUIZ_PLAYING,
                        SessionState.INTERIM_ANNOUNCEMENT,
                    ),
                ),
                arguments(
                    SessionState.QUIZ_PLAYING,
                    listOf(
                        SessionState.QUIZ_RESULT,
                    ),
                ),
                arguments(
                    SessionState.QUIZ_RESULT,
                    listOf(
                        SessionState.FINAL_RESULT_ANNOUNCEMENT,
                        SessionState.QUIZ_WAITING,
                    ),
                ),
                arguments(
                    SessionState.FINAL_RESULT_ANNOUNCEMENT,
                    listOf(
                        SessionState.FINISHED,
                    ),
                ),
                arguments(
                    SessionState.FINISHED,
                    listOf<SessionState>(),
                ),
                arguments(
                    SessionState.INTERIM_ANNOUNCEMENT,
                    listOf(
                        SessionState.QUIZ_WAITING,
                    ),
                ),
            )
        }
    }
}
