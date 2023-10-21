package wedding.kanshasai.backend.domain.value

import wedding.kanshasai.backend.annotation.NoArg
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.state.SessionState

@NoArg
class ResultType private constructor(val value: ResultTypeEnum) {
    override fun toString(): String = value.name

    fun toNumber(): Int = value.number

    fun toGrpcType(): wedding.kanshasai.v1.ResultType = value.grpcValue

    val sessionState get() = value.sessionState

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResultType

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    enum class ResultTypeEnum(
        val grpcValue: wedding.kanshasai.v1.ResultType,
        val sessionState: SessionState,
        val number: Int = grpcValue.number,
    ) {
        INTERIM(
            wedding.kanshasai.v1.ResultType.RESULT_TYPE_INTERIM,
            SessionState.INTERIM_ANNOUNCEMENT,
        ),
        FINAL(
            wedding.kanshasai.v1.ResultType.RESULT_TYPE_FINAL,
            SessionState.FINAL_RESULT_ANNOUNCEMENT,
        ),
    }

    companion object {
        fun of(value: Int): ResultType {
            values.forEach {
                if (it.toNumber() == value) return it
            }
            throw InvalidArgumentException("Invalid ResultType value. (value=$value)")
        }

        val values = ResultType.ResultTypeEnum.values().map(::ResultType)

        val INTERIM = ResultType(ResultTypeEnum.INTERIM)
        val FINAL = ResultType(ResultTypeEnum.FINAL)
    }
}
