package wedding.kanshasai.backend.domain.state

import io.github.oshai.kotlinlogging.KotlinLogging
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateTransitionException

private val logger = KotlinLogging.logger {}

class SessionState private constructor(private val value: SessionStateEnum) {

    override fun toString(): String = value.name
    fun toNumber(): Int = value.number

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SessionState

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    private enum class SessionStateEnum(
        val number: Int,
    ) {
        INTRODUCTION(1) {
            override val nextStateList
                get() = listOf(QUIZ_WAITING)
        },
        QUIZ_WAITING(10) {
            override val nextStateList
                get() = listOf(QUIZ_ANSWERING, INTERIM_ANNOUNCEMENT)
        },
        QUIZ_ANSWERING(20) {
            override val nextStateList
                get() = listOf(QUIZ_DEADLINE_PASSED)
        },
        QUIZ_DEADLINE_PASSED(30) {
            override val nextStateList
                get() = listOf(QUIZ_CORRECT_ANSWER)
        },
        QUIZ_CORRECT_ANSWER(40) {
            override val nextStateList
                get() = listOf(QUIZ_FASTEST_RANKING)
        },
        QUIZ_FASTEST_RANKING(50) {
            override val nextStateList
                get() = listOf(FINAL_RESULT_ANNOUNCEMENT, QUIZ_WAITING)
        },
        FINAL_RESULT_ANNOUNCEMENT(60) {
            override val nextStateList
                get() = listOf(FINISHED)
        },
        FINISHED(9999) {
            override val nextStateList
                get() = listOf<SessionStateEnum>()
        },
        INTERIM_ANNOUNCEMENT(1000) {
            override val nextStateList
                get() = listOf(QUIZ_WAITING)
        },
        ;
        abstract val nextStateList: List<SessionStateEnum>
    }

    fun next(nextState: SessionState) = runCatching {
        if (this == nextState) {
            logger.warn { "Recursive state transition from '${value.name}' to '${nextState.value.name}' has occurred" }
            return@runCatching nextState
        }
        if (!value.nextStateList.contains(nextState.value)) {
            throw InvalidStateTransitionException("Transition from '${value.name}' to '${nextState.value.name}' is invalid")
        }
        return@runCatching nextState
    }

    companion object {
        fun of(value: Int): SessionState {
            if (value == 1) return SessionState(SessionStateEnum.INTRODUCTION)
            if (value == 10) return SessionState(SessionStateEnum.QUIZ_WAITING)
            if (value == 20) return SessionState(SessionStateEnum.QUIZ_ANSWERING)
            if (value == 30) return SessionState(SessionStateEnum.QUIZ_DEADLINE_PASSED)
            if (value == 40) return SessionState(SessionStateEnum.QUIZ_CORRECT_ANSWER)
            if (value == 50) return SessionState(SessionStateEnum.QUIZ_FASTEST_RANKING)
            if (value == 60) return SessionState(SessionStateEnum.FINAL_RESULT_ANNOUNCEMENT)
            if (value == 9999) return SessionState(SessionStateEnum.FINISHED)
            if (value == 1000) return SessionState(SessionStateEnum.INTERIM_ANNOUNCEMENT)
            throw InvalidArgumentException("Invalid SessionState value. (value=$value)")
        }

        val INTRODUCTION = SessionState(SessionStateEnum.INTRODUCTION)
        val QUIZ_WAITING = SessionState(SessionStateEnum.QUIZ_WAITING)
        val QUIZ_ANSWERING = SessionState(SessionStateEnum.QUIZ_ANSWERING)
        val QUIZ_DEADLINE_PASSED = SessionState(SessionStateEnum.QUIZ_DEADLINE_PASSED)
        val QUIZ_CORRECT_ANSWER = SessionState(SessionStateEnum.QUIZ_CORRECT_ANSWER)
        val QUIZ_FASTEST_RANKING = SessionState(SessionStateEnum.QUIZ_FASTEST_RANKING)
        val FINAL_RESULT_ANNOUNCEMENT = SessionState(SessionStateEnum.FINAL_RESULT_ANNOUNCEMENT)
        val FINISHED = SessionState(SessionStateEnum.FINISHED)
        val INTERIM_ANNOUNCEMENT = SessionState(SessionStateEnum.INTERIM_ANNOUNCEMENT)
    }
}
