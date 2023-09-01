package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.CoverScreenType as CoverScreenTypeGrpc

@NoArg
class CoverScreenType private constructor(val value: CoverScreenTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): CoverScreenTypeGrpc = value.grpcValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoverScreenType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    enum class CoverScreenTypeEnum(
        val number: Int,
        val grpcValue: CoverScreenTypeGrpc,
    ) {
        WELCOME(1, CoverScreenTypeGrpc.COVER_SCREEN_TYPE_WELCOME),
        RULE(2, CoverScreenTypeGrpc.COVER_SCREEN_TYPE_RULE),
        COVER(9, CoverScreenTypeGrpc.COVER_SCREEN_TYPE_COVER),
    }

    companion object {
        fun of(value: Int): CoverScreenType {
            if (value == 1) return CoverScreenType(CoverScreenTypeEnum.WELCOME)
            if (value == 2) return CoverScreenType(CoverScreenTypeEnum.RULE)
            if (value == 9) return CoverScreenType(CoverScreenTypeEnum.COVER)
            throw InvalidArgumentException("Invalid CoverScreenType value. (value=$value)")
        }
    }
}
