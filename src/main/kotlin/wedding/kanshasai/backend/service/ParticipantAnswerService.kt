package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.*

@Service
class ParticipantAnswerService(
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val participantRepository: ParticipantRepository,
    private val participantAnswerRepository: ParticipantAnswerRepository,
) {
    fun setAnswer(participantId: UlidId, quizId: UlidId, answer: String, time: Float) {
        val participant = participantRepository.findById(participantId).getOrThrowService()
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val session = sessionRepository.findById(participant.sessionId).getOrThrowService()
        if (session.currentQuizId != quizId) {
            throw InvalidArgumentException("Current quiz id is not $quizId.")
        }

        if (session.state != SessionState.QUIZ_PLAYING) {
            throw InvalidStateException("Session state is not QUIZ_PLAYING.")
        }

        if (participantAnswerRepository.find(session, quiz, participant).isSuccess) {
            throw InvalidStateException("Participant answer is already set.")
        }

        participantAnswerRepository.createParticipantAnswer(session, quiz, participant, answer, time).getOrThrowService()
    }
}