package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.IntroductionScreenType as IntroductionScreenTypeGrpc

@NoArg
class IntroductionType private constructor(val value: IntroductionScreenTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): IntroductionScreenTypeGrpc = value.grpcValue

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
        val number: Int,
        val grpcValue: IntroductionScreenTypeGrpc,
    ) {
        WELCOME(1, IntroductionScreenTypeGrpc.INTRODUCTION_SCREEN_TYPE_WELCOME),
        RULE(2, IntroductionScreenTypeGrpc.INTRODUCTION_SCREEN_TYPE_RULE),
        Introduction(3, IntroductionScreenTypeGrpc.INTRODUCTION_SCREEN_TYPE_LOGO),
    }

    companion object {
        fun of(value: Int): IntroductionType {
            if (value == 1) return IntroductionType(IntroductionScreenTypeEnum.WELCOME)
            if (value == 2) return IntroductionType(IntroductionScreenTypeEnum.RULE)
            if (value == 3) return IntroductionType(IntroductionScreenTypeEnum.Introduction)
            throw InvalidArgumentException("Invalid CoverScreenType value. (value=$value)")
        }

        val values = IntroductionScreenTypeEnum.values().map { IntroductionType(it) }
    }
}
