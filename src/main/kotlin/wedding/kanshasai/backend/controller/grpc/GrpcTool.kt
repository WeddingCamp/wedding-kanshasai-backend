package wedding.kanshasai.backend.controller.grpc

import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.domain.value.CoverScreenType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.v1.CoverScreenType as CoverScreenTypeGrpc

@Component
class GrpcTool {
    fun parseUlidId(rawUlidId: String, fieldName: String): UlidId {
        return try { UlidId.of(rawUlidId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'$fieldName' cannot be parsed as ULID format.", e)
        }
    }

    fun parseCoverScreenType(screenType: CoverScreenTypeGrpc): CoverScreenType {
        return try { CoverScreenType.of(screenType.number) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("Invalid cover screen type (name='${screenType.name}', number='${screenType.number}')", e)
        } catch (e: IllegalArgumentException) {
            throw InvalidArgumentException("Invalid cover screen type (name='${screenType.name}')", e)
        }
    }
}
