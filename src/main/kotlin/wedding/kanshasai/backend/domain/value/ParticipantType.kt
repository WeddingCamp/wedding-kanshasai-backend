package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.v1.ParticipantType as ParticipantTypeGrpc

@NoArg
class ParticipantType private constructor(val value: ParticipantTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): ParticipantTypeGrpc = value.grpcValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParticipantType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    enum class ParticipantTypeEnum(
        val grpcValue: ParticipantTypeGrpc,
        val number: Int = grpcValue.number,
    ) {
        GROOM(ParticipantTypeGrpc.PARTICIPANT_TYPE_GROOM),
        BRIDE(ParticipantTypeGrpc.PARTICIPANT_TYPE_BRIDE),
    }

    companion object {
        fun of(value: Int): ParticipantType {
            values.forEach {
                if (it.toNumber() == value) return it
            }
            throw InvalidArgumentException("Invalid ParticipantType value. (value=$value)")
        }

        val values = ParticipantTypeEnum.values().map(::ParticipantType)
    }
}
