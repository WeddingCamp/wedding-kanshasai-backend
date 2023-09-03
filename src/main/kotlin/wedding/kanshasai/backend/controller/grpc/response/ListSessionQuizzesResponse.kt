package wedding.kanshasai.backend.controller.grpc.response

import wedding.kanshasai.backend.domain.entity.Choice
import wedding.kanshasai.backend.domain.entity.Quiz
import wedding.kanshasai.backend.domain.entity.SessionQuiz
import wedding.kanshasai.v1.ListSessionQuizzesResponse

fun ListSessionQuizzesResponse.SessionQuiz.Builder.setQuiz(quiz: Quiz): ListSessionQuizzesResponse.SessionQuiz.Builder = apply {
    quizId = quiz.id.toString()
    body = quiz.body
    quizType = quiz.type.toGrpcType()
}

fun ListSessionQuizzesResponse.SessionQuiz.Builder.setSessionQuiz(sessionQuiz: SessionQuiz):
    ListSessionQuizzesResponse.SessionQuiz.Builder = apply {
    isCompleted = sessionQuiz.isCompleted
}

fun ListSessionQuizzesResponse.Choice.Builder.setChoice(choice: Choice): ListSessionQuizzesResponse.Choice.Builder = apply {
    choiceId = choice.id.toString()
    body = choice.body
}
