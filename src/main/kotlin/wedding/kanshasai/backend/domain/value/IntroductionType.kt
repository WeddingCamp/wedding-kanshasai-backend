package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.IntroductionType as IntroductionTypeGrpc

@NoArg
class IntroductionType private constructor(val value: IntroductionScreenTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): IntroductionTypeGrpc = value.grpcValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntroductionType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    enum class IntroductionScreenTypeEnum(
        val grpcValue: IntroductionTypeGrpc,
        val number: Int = grpcValue.number,
    ) {
        WELCOME(IntroductionTypeGrpc.INTRODUCTION_TYPE_WELCOME),
        RULE(IntroductionTypeGrpc.INTRODUCTION_TYPE_RULE),
        LOGO(IntroductionTypeGrpc.INTRODUCTION_TYPE_LOGO),
    }

    companion object {
        fun of(value: Int): IntroductionType {
            values.forEach {
                if (it.toNumber() == value) return it
            }
            throw InvalidArgumentException("Invalid IntroductionType value. (value=$value)")
        }

        val values = IntroductionScreenTypeEnum.values().map(::IntroductionType)
    }
}
