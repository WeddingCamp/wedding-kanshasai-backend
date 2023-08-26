package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.CoverScreenType as CoverScreenTypeGrpc

class CoverScreenType private constructor(private val type: CoverScreenTypeEnum) {
    val value: Int get() = type.value

    override fun toString(): String {
        return type.name
    }

    fun toGrpcType(): CoverScreenTypeGrpc {
        return type.grpcValue
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoverScreenType

        return type == other.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    private enum class CoverScreenTypeEnum(
        val value: Int,
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
            throw InvalidArgumentException("Invalid CoverScreenType value.")
        }
    }
}
