package wedding.kanshasai.backend.domain.state

import io.github.oshai.kotlinlogging.KotlinLogging
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException

private val logger = KotlinLogging.logger {}

enum class SessionState(val id: Int) {
    BEFORE_START(1) {
        override val nextStateList
            get() = listOf(EXPLAINING)
    },
    EXPLAINING(10) {
        override val nextStateList
            get() = listOf(QUIZ_WAITING)
    },
    QUIZ_WAITING(20) {
        override val nextStateList
            get() = listOf(QUIZ_ANSWERING, INTERIM_ANNOUNCEMENT)
    },
    QUIZ_ANSWERING(30) {
        override val nextStateList
            get() = listOf(QUIZ_DEADLINE_PASSED)
    },
    QUIZ_DEADLINE_PASSED(40) {
        override val nextStateList
            get() = listOf(QUIZ_CORRECT_ANSWER)
    },
    QUIZ_CORRECT_ANSWER(50) {
        override val nextStateList
            get() = listOf(QUIZ_FASTEST_RANKING)
    },
    QUIZ_FASTEST_RANKING(60) {
        override val nextStateList
            get() = listOf(FINAL_RESULT_ANNOUNCEMENT, QUIZ_WAITING)
    },
    FINAL_RESULT_ANNOUNCEMENT(70) {
        override val nextStateList
            get() = listOf(FINISHED)
    },
    FINISHED(9999) {
        override val nextStateList
            get() = listOf<SessionState>()
    },
    INTERIM_ANNOUNCEMENT(1000) {
        override val nextStateList
            get() = listOf(QUIZ_WAITING)
    },
    ;

    abstract val nextStateList: List<SessionState>

    fun next(nextState: SessionState) = runCatching {
        if (this == nextState) {
            logger.warn { "Recursive state transition from '${this.name}' to '${nextState.name}' has occurred" }
            return@runCatching nextState
        }
        if (!nextStateList.contains(nextState)) {
            throw InvalidStateTransitionException("Transition from '${this.name}' to '${nextState.name}' is invalid")
        }
        return@runCatching nextState
    }
}
