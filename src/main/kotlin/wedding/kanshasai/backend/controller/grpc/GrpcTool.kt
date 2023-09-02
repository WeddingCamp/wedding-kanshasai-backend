package wedding.kanshasai.backend.controller.grpc

import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.domain.value.IntroductionType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.v1.IntroductionScreenType as IntroductionScreenTypeGrpc

@Component
class GrpcTool {
    fun parseUlidId(rawUlidId: String, fieldName: String): UlidId {
        return try { UlidId.of(rawUlidId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'$fieldName' cannot be parsed as ULID format.", e)
        }
    }

    fun parseIntroductionType(introductionType: IntroductionScreenTypeGrpc): IntroductionType {
        return try { IntroductionType.of(introductionType.number) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("Invalid introduction type (name='${introductionType.name}', number='${introductionType.number}')", e)
        } catch (e: IllegalArgumentException) {
            throw InvalidArgumentException("Invalid introduction type (name='${introductionType.name}')", e)
        }
    }
}
