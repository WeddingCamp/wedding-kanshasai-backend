package wedding.kanshasai.backend.controller.grpc

import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.domain.value.IntroductionType
import wedding.kanshasai.backend.domain.value.QuizResultType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.v1.IntroductionType as IntroductionTypeGrpc
import wedding.kanshasai.v1.QuizResultScreenType as QuizResultScreenTypeGrpc

@Component
class GrpcTool {
    fun parseUlidId(rawUlidId: String, fieldName: String): UlidId {
        if (rawUlidId.isEmpty()) throw InvalidArgumentException.requiredField(fieldName)
        return try { UlidId.of(rawUlidId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'$fieldName' cannot be parsed as ULID format.", e)
        }
    }

    fun parseIntroductionType(introductionType: IntroductionTypeGrpc): IntroductionType {
        return try { IntroductionType.of(introductionType.number) } catch (e: InvalidValueException) {
            throw InvalidArgumentException(
                "Invalid introduction type (name='${introductionType.name}', number='${introductionType.number}')",
                e,
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidArgumentException("Invalid introduction type (name='${introductionType.name}')", e)
        }
    }

    fun parseQuizResultScreenType(quizResultScreenType: QuizResultScreenTypeGrpc): QuizResultType {
        return try { QuizResultType.of(quizResultScreenType.number) } catch (e: InvalidValueException) {
            throw InvalidArgumentException(
                "Invalid quiz result screen type (name='${quizResultScreenType.name}', number='${quizResultScreenType.number}')",
                e,
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidArgumentException("Invalid quiz result screen type (name='${quizResultScreenType.name}')", e)
        }
    }
}
