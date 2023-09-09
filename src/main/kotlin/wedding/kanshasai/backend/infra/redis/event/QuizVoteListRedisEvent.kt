package wedding.kanshasai.backend.infra.redis.event

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.infra.redis.entity.QuizChoiceWithCountRedisEntity
import wedding.kanshasai.v1.QuizType

@NoArg
data class QuizVoteListRedisEvent(
    override var quizId: String,
    override var quizBody: String,
    override var quizType: QuizType,
    override var choiceList: List<QuizChoiceWithCountRedisEntity>,
) : AbstractQuizRedisEvent<QuizChoiceWithCountRedisEntity>(quizId, quizBody, quizType, choiceList)
