package wedding.kanshasai.backend.domain.state

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import wedding.kanshasai.backend.exception.InvalidStateTransitionException
import java.util.stream.Stream

class SessionStateTest {
    @ParameterizedTest
    @MethodSource("sessionStateProvider")
    fun next_shouldProperlyThrowException(pattern: SessionStatePattern) {
        SessionState.values().forEach {
            if (it == pattern.sessionState || pattern.validTransitionDestinationList.contains(it)) {
                assertDoesNotThrow {
                    pattern.sessionState.next(it).getOrThrow()
                }
            } else {
                assertThrows<InvalidStateTransitionException> {
                    pattern.sessionState.next(it).getOrThrow()
                }
            }
        }
    }

    data class SessionStatePattern(
        val sessionState: SessionState,
        val validTransitionDestinationList: List<SessionState>,
    )

    companion object {
        @JvmStatic
        private fun sessionStateProvider(): Stream<Arguments> {
            return Stream.of(
                arguments(
                    SessionStatePattern(
                        SessionState.BEFORE_START,
                        listOf(
                            SessionState.EXPLAINING,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.EXPLAINING,
                        listOf(
                            SessionState.QUIZ_WAITING,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.QUIZ_WAITING,
                        listOf(
                            SessionState.QUIZ_ANSWERING,
                            SessionState.INTERIM_ANNOUNCEMENT,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.QUIZ_ANSWERING,
                        listOf(
                            SessionState.QUIZ_DEADLINE_PASSED,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.QUIZ_DEADLINE_PASSED,
                        listOf(
                            SessionState.QUIZ_CORRECT_ANSWER,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.QUIZ_CORRECT_ANSWER,
                        listOf(
                            SessionState.QUIZ_FASTEST_RANKING,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.QUIZ_FASTEST_RANKING,
                        listOf(
                            SessionState.FINAL_RESULT_ANNOUNCEMENT,
                            SessionState.QUIZ_WAITING,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.FINAL_RESULT_ANNOUNCEMENT,
                        listOf(
                            SessionState.FINISHED,
                        ),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.FINISHED,
                        listOf(),
                    ),
                ),
                arguments(
                    SessionStatePattern(
                        SessionState.INTERIM_ANNOUNCEMENT,
                        listOf(
                            SessionState.QUIZ_WAITING,
                        ),
                    ),
                ),
            )
        }
    }
}
