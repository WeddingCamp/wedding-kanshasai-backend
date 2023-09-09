package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.QuizResultScreenType as QuizResultScreenTypeGrpc

@NoArg
class QuizResultType private constructor(val value: ResultScreenTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): QuizResultScreenTypeGrpc = value.grpcValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizResultType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    enum class ResultScreenTypeEnum(
        val grpcValue: QuizResultScreenTypeGrpc,
        val number: Int = grpcValue.number,
    ) {
        VOTE_LIST(QuizResultScreenTypeGrpc.QUIZ_RESULT_SCREEN_TYPE_VOTE_LIST),
        RESULT(QuizResultScreenTypeGrpc.QUIZ_RESULT_SCREEN_TYPE_RESULT),
        FASTEST_RANKING(QuizResultScreenTypeGrpc.QUIZ_RESULT_SCREEN_TYPE_FASTEST_RANKING),
    }

    companion object {
        fun of(value: Int): QuizResultType {
            values.forEach {
                if (it.toNumber() == value) return it
            }
            throw InvalidArgumentException("Invalid QuizResultScreenType value. (value=$value)")
        }

        val values = QuizResultType.ResultScreenTypeEnum.values().map(::QuizResultType)

        val VOTE_LIST = QuizResultType(ResultScreenTypeEnum.VOTE_LIST)
        val RESULT = QuizResultType(ResultScreenTypeEnum.RESULT)
        val FASTEST_RANKING = QuizResultType(ResultScreenTypeEnum.FASTEST_RANKING)
    }
}
