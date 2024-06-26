package wedding.kanshasai.backend.controller.grpc

import org.springframework.stereotype.Component
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidValueException
import wedding.kanshasai.backend.domain.value.QuizResultType
import wedding.kanshasai.backend.domain.value.ResultType
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.v1.QuizResultType as QuizResultTypeGrpc
import wedding.kanshasai.v1.ResultType as ResultTypeGrpc

@Component
class GrpcTool {
    fun parseUlidId(rawUlidId: String, fieldName: String): UlidId {
        if (rawUlidId.isEmpty()) throw InvalidArgumentException.requiredField(fieldName)
        return try { UlidId.of(rawUlidId) } catch (e: InvalidValueException) {
            throw InvalidArgumentException("'$fieldName' cannot be parsed as ULID format.", e)
        }
    }

    fun parseQuizResultType(quizResultType: QuizResultTypeGrpc): QuizResultType {
        return try { QuizResultType.of(quizResultType.number) } catch (e: InvalidValueException) {
            throw InvalidArgumentException(
                "Invalid quiz result type (name='${quizResultType.name}', number='${quizResultType.number}')",
                e,
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidArgumentException("Invalid quiz result type (name='${quizResultType.name}')", e)
        }
    }

    fun parseQuizResultType(resultType: ResultTypeGrpc): ResultType {
        return try { ResultType.of(resultType.number) } catch (e: InvalidValueException) {
            throw InvalidArgumentException(
                "Invalid result type (name='${resultType.name}', number='${resultType.number}')",
                e,
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidArgumentException("Invalid result type (name='${resultType.name}')", e)
        }
    }
}
