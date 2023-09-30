package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.value.IntroductionType
import wedding.kanshasai.backend.infra.redis.entity.*
import wedding.kanshasai.v1.EventType
import wedding.kanshasai.v1.QuizType
import wedding.kanshasai.v1.SimpleSessionState

interface RedisEvent {
    val eventType: EventType
    val sessionId: String

    @NoArg
    data class QuizSpeedRanking(
        var participantQuizTimeList: List<ParticipantQuizTimeRedisEntity>,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_SPEED_RANKING
    }

    @NoArg
    data class ShowQuiz(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceRedisEntity>,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList) {
        override val eventType: EventType = EventType.EVENT_TYPE_SHOW_QUIZ
    }

    @NoArg
    data class Introduction(
        var introductionType: IntroductionType,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_INTRODUCTION
    }

    @NoArg
    data class StartQuiz(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceRedisEntity>,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList) {
        override val eventType: EventType = EventType.EVENT_TYPE_START_QUIZ
    }

    @NoArg
    data class QuizAnswerList(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceWithCountRedisEntity>,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceWithCountRedisEntity>(quizId, quizBody, quizType, choiceList) {
        override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_ANSWER_LIST
    }

    @NoArg
    data class CurrentState(
        val simpleSessionState: SimpleSessionState,
        val sessionState: String,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_CURRENT_STATE
    }

    @NoArg
    data class PreQuiz(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceRedisEntity>,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList) {
        override val eventType: EventType = EventType.EVENT_TYPE_PRE_QUIZ
    }

    @NoArg
    data class QuizResult(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceWithResultRedisEntity>,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceWithResultRedisEntity>(quizId, quizBody, quizType, choiceList) {
        override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_RESULT
    }

    @NoArg
    data class Cover(
        var isVisible: Boolean,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_COVER
    }

    @NoArg
    data class UpdateParticipant(
        var participantList: List<ParticipantRedisEntity>,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_UPDATE_PARTICIPANT
    }

    @NoArg
    abstract class AbstractQuizRedisEvent<CHOICE : AbstractQuizChoiceRedisEntity>(
        open var quizId: String,
        open var quizBody: String,
        open var quizType: QuizType,
        open var choiceList: List<CHOICE>,
    ) : RedisEvent
}
