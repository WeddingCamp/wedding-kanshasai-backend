package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.*
import wedding.kanshasai.v1.*

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
        override var quizNumber: Int,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList, quizNumber) {
        override val eventType: EventType = EventType.EVENT_TYPE_SHOW_QUIZ
    }

    @NoArg
    data class Introduction(
        var introductionId: Int,
        var isFirst: Boolean,
        var isLast: Boolean,
        var qrCodeImageUrl: String,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_INTRODUCTION
    }

    @NoArg
    data class ShowProfile(
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_SHOW_PROFILE
    }

    @NoArg
    data class StartQuiz(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceRedisEntity>,
        override var quizNumber: Int,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList, quizNumber) {
        override val eventType: EventType = EventType.EVENT_TYPE_START_QUIZ
    }

    @NoArg
    data class QuizAnswerList(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceWithCountRedisEntity>,
        override var quizNumber: Int,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceWithCountRedisEntity>(quizId, quizBody, quizType, choiceList, quizNumber) {
        override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_ANSWER_LIST
    }

    @NoArg
    data class QuizTimeUp(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceRedisEntity>,
        override var quizNumber: Int,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList, quizNumber) {
        override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_TIME_UP
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
    data class FullCurrentState(
        val simpleSessionState: SimpleSessionState,
        val sessionState: String,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_FULL_CURRENT_STATE
    }

    @NoArg
    data class PreQuiz(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceRedisEntity>,
        override var quizNumber: Int,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceRedisEntity>(quizId, quizBody, quizType, choiceList, quizNumber) {
        override val eventType: EventType = EventType.EVENT_TYPE_PRE_QUIZ
    }

    @NoArg
    data class QuizResult(
        override var quizId: String,
        override var quizBody: String,
        override var quizType: QuizType,
        override var choiceList: List<QuizChoiceWithResultRedisEntity>,
        override var quizNumber: Int,
        override val sessionId: String,
    ) : AbstractQuizRedisEvent<QuizChoiceWithResultRedisEntity>(quizId, quizBody, quizType, choiceList, quizNumber) {
        override val eventType: EventType = EventType.EVENT_TYPE_QUIZ_RESULT
    }

    @NoArg
    data class CancelQuiz(
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_CANCEL_QUIZ
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
    data class ShowResultTitle(
        var resultType: ResultType,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_RESULT_TITLE
    }

    @NoArg
    data class ShowResultRankingTitle(
        var rank: Int,
        var resultTitleType: ResultTitleType,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_RESULT_RANKING_TITLE
    }

    @NoArg
    data class ShowResultRanking(
        var participantSessionScoreList: List<ParticipantSessionScoreRedisEntity>,
        var preDisplayCount: Int,
        var displayCount: Int,
        var resultRankingType: ResultRankingType,
        var hasNextPage: Boolean,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_RESULT_RANKING
    }

    @NoArg
    data class ShowResultPresent(
        var rank: Int,
        var resultRankingType: ResultPresentType,
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_RESULT_PRESENT
    }

    @NoArg
    data class Finish(
        override val sessionId: String,
    ) : RedisEvent {
        override val eventType: EventType = EventType.EVENT_TYPE_FINISH
    }

    @NoArg
    abstract class AbstractQuizRedisEvent<CHOICE : AbstractQuizChoiceRedisEntity>(
        open var quizId: String,
        open var quizBody: String,
        open var quizType: QuizType,
        open var choiceList: List<CHOICE>,
        open var quizNumber: Int,
    ) : RedisEvent
}
