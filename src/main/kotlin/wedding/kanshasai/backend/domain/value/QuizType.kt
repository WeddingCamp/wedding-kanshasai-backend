package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.QuizType as QuizTypeGrpc

class QuizType private constructor(private val value: QuizTypeEnum) {
    val number: Int get() = value.number

    override fun toString(): String {
        return value.name
    }

    fun toGrpcType(): QuizTypeGrpc {
        return value.grpcValue
    }

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
        val number: Int,
        val grpcValue: QuizTypeGrpc,
    ) {
        FOUR_CHOICES_QUIZ(1, QuizTypeGrpc.QUIZ_TYPE_FOUR_CHOICES_QUIZ),
        SORT_IMAGE_QUIZ(2, QuizTypeGrpc.QUIZ_TYPE_SORT_IMAGE_QUIZ),
        REALTIME_FOUR_CHOICE_QUIZ(3, QuizTypeGrpc.QUIZ_TYPE_SORT_IMAGE_QUIZ), // TODO: QUIZ_TYPE_REALTIME_FOUR_CHOICE_QUIZに変更する
    }

    companion object {
        fun of(value: Int): QuizType {
            if (value == 1) return QuizType(QuizTypeEnum.FOUR_CHOICES_QUIZ)
            if (value == 2) return QuizType(QuizTypeEnum.SORT_IMAGE_QUIZ)
            if (value == 3) return QuizType(QuizTypeEnum.REALTIME_FOUR_CHOICE_QUIZ)
            throw InvalidArgumentException("Invalid QuizType value. (value=$value)")
        }
    }
}
