package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.QuizResultType as QuizResultTypeGrpc

@NoArg
class QuizResultType private constructor(val value: QuizResultTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): QuizResultTypeGrpc = value.grpcValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizResultType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    enum class QuizResultTypeEnum(
        val grpcValue: QuizResultTypeGrpc,
        val number: Int = grpcValue.number,
    ) {
        VOTE_LIST(QuizResultTypeGrpc.QUIZ_RESULT_TYPE_VOTE_LIST),
        RESULT(QuizResultTypeGrpc.QUIZ_RESULT_TYPE_RESULT),
        FASTEST_RANKING(QuizResultTypeGrpc.QUIZ_RESULT_TYPE_FASTEST_RANKING),
    }

    companion object {
        fun of(value: Int): QuizResultType {
            values.forEach {
                if (it.toNumber() == value) return it
            }
            throw InvalidArgumentException("Invalid QuizResultType value. (value=$value)")
        }

        val values = QuizResultType.QuizResultTypeEnum.values().map(::QuizResultType)

        val VOTE_LIST = QuizResultType(QuizResultTypeEnum.VOTE_LIST)
        val RESULT = QuizResultType(QuizResultTypeEnum.RESULT)
        val FASTEST_RANKING = QuizResultType(QuizResultTypeEnum.FASTEST_RANKING)
    }
}
