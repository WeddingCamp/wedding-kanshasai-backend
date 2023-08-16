package wedding.kanshasai.backend.domain

import io.github.oshai.kotlinlogging.KotlinLogging
import wedding.kanshasai.backend.exception.InvalidStateTransitionException

private val logger = KotlinLogging.logger {}

enum class SessionState {
    BEFORE_START {
        override val nextStateList
            get() = listOf(EXPLAINING)
    },
    EXPLAINING {
        override val nextStateList
            get() = listOf(QUIZ_WAITING)
    },
    QUIZ_WAITING {
        override val nextStateList
            get() = listOf(QUIZ_ANSWERING, INTERIM_ANNOUNCEMENT)
    },
    QUIZ_ANSWERING {
        override val nextStateList
            get() = listOf(QUIZ_DEADLINE_PASSED)
    },
    QUIZ_DEADLINE_PASSED {
        override val nextStateList
            get() = listOf(QUIZ_CORRECT_ANSWER)
    },
    QUIZ_CORRECT_ANSWER {
        override val nextStateList
            get() = listOf(QUIZ_FASTEST_RANKING)
    },
    QUIZ_FASTEST_RANKING {
        override val nextStateList
            get() = listOf(FINAL_RESULT_ANNOUNCEMENT, QUIZ_WAITING)
    },
    FINAL_RESULT_ANNOUNCEMENT {
        override val nextStateList
            get() = listOf(FINISHED)
    },
    FINISHED {
        override val nextStateList
            get() = listOf<SessionState>()
    },
    INTERIM_ANNOUNCEMENT {
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
