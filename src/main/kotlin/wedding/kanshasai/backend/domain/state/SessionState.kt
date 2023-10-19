package wedding.kanshasai.backend.domain.state

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.SimpleSessionState

class SessionState private constructor(override val value: SessionStateEnum) :
    StateMachine<SessionState, SessionState.SessionStateEnum>(value) {
        fun toSimpleSessionState(): SimpleSessionState {
            if (this == INTRODUCTION) return SimpleSessionState.SIMPLE_SESSION_STATE_BEFORE_START
            if (this == FINISHED) return SimpleSessionState.SIMPLE_SESSION_STATE_FINISHED
            return SimpleSessionState.SIMPLE_SESSION_STATE_DURING_THE_GAME
        }

        enum class SessionStateEnum(
            override val number: Int,
        ) : StateInterface {
            INTRODUCTION(1) {
                override val nextStateList
                    get() = listOf(INTRODUCTION, QUIZ_WAITING)
            },
            QUIZ_WAITING(10) {
                override val nextStateList
                    get() = listOf(QUIZ_WAITING, QUIZ_SHOWING, INTERIM_ANNOUNCEMENT)
            },
            QUIZ_SHOWING(15) {
                override val nextStateList
                    get() = listOf(QUIZ_PLAYING)
            },
            QUIZ_PLAYING(20) {
                override val nextStateList
                    get() = listOf(QUIZ_CLOSED)
            },
            QUIZ_CLOSED(30) {
                override val nextStateList
                    get() = listOf(QUIZ_RESULT)
            },
            QUIZ_RESULT(50) {
                override val nextStateList
                    get() = listOf(QUIZ_RESULT, FINAL_RESULT_ANNOUNCEMENT, QUIZ_WAITING)
            },
            FINAL_RESULT_ANNOUNCEMENT(60) {
                override val nextStateList
                    get() = listOf(FINAL_RESULT_ANNOUNCEMENT, FINISHED)
            },
            FINISHED(9999) {
                override val nextStateList
                    get() = listOf<SessionStateEnum>()
            },
            INTERIM_ANNOUNCEMENT(1000) {
                override val nextStateList
                    get() = listOf(INTERIM_ANNOUNCEMENT, QUIZ_WAITING)
            },
        }

        override fun equals(other: Any?): Boolean {
            if (other !is SessionState) return false
            if (value != other.value) return false
            return true
        }

        companion object {
            fun of(value: Int): SessionState {
                SessionStateEnum.values().forEach {
                    if (it.number == value) return SessionState(it)
                }
                throw InvalidArgumentException("Invalid SessionState value. (value=$value)")
            }

            val INTRODUCTION = SessionState(SessionStateEnum.INTRODUCTION)
            val QUIZ_WAITING = SessionState(SessionStateEnum.QUIZ_WAITING)
            val QUIZ_SHOWING = SessionState(SessionStateEnum.QUIZ_SHOWING)
            val QUIZ_PLAYING = SessionState(SessionStateEnum.QUIZ_PLAYING)
            val QUIZ_CLOSED = SessionState(SessionStateEnum.QUIZ_CLOSED)
            val QUIZ_RESULT = SessionState(SessionStateEnum.QUIZ_RESULT)
            val FINAL_RESULT_ANNOUNCEMENT = SessionState(SessionStateEnum.FINAL_RESULT_ANNOUNCEMENT)
            val FINISHED = SessionState(SessionStateEnum.FINISHED)
            val INTERIM_ANNOUNCEMENT = SessionState(SessionStateEnum.INTERIM_ANNOUNCEMENT)

            val values = SessionStateEnum.values().map { SessionState(it) }
        }
    }
