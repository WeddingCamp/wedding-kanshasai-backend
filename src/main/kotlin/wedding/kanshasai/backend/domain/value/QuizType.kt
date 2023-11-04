package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.QuizType as QuizTypeGrpc

class QuizType private constructor(private val value: QuizTypeEnum) {

    override fun toString(): String = value.name
    fun toNumber(): Int = value.number
    fun toGrpcType(): QuizTypeGrpc = value.grpcValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    private enum class QuizTypeEnum(
        val grpcValue: QuizTypeGrpc,
        val number: Int = grpcValue.number,
    ) {
        FOUR_CHOICES_QUIZ(QuizTypeGrpc.QUIZ_TYPE_FOUR_CHOICES_QUIZ),
        SORT_IMAGE_QUIZ(QuizTypeGrpc.QUIZ_TYPE_SORT_IMAGE_QUIZ),
        REALTIME_FOUR_CHOICE_QUIZ(QuizTypeGrpc.QUIZ_TYPE_REALTIME_FOUR_CHOICE_QUIZ),
    }

    companion object {
        fun of(value: Int): QuizType {
            if (value == 1) return QuizType(QuizTypeEnum.FOUR_CHOICES_QUIZ)
            if (value == 2) return QuizType(QuizTypeEnum.SORT_IMAGE_QUIZ)
            if (value == 3) return QuizType(QuizTypeEnum.REALTIME_FOUR_CHOICE_QUIZ)
            throw InvalidArgumentException("Invalid QuizType value. (value=$value)")
        }

        val values = QuizType.QuizTypeEnum.values().map(::QuizType)

        val FOUR_CHOICES_QUIZ = QuizType(QuizTypeEnum.FOUR_CHOICES_QUIZ)
        val SORT_IMAGE_QUIZ = QuizType(QuizTypeEnum.SORT_IMAGE_QUIZ)
        val REALTIME_FOUR_CHOICE_QUIZ = QuizType(QuizTypeEnum.REALTIME_FOUR_CHOICE_QUIZ)
    }
}
