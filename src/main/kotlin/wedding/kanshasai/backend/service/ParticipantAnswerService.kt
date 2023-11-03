package wedding.kanshasai.backend.service

import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.exception.InvalidArgumentException
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.state.SessionState
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.mysql.repository.*
import wedding.kanshasai.backend.infra.redis.entity.ParticipantRedisEntity
import wedding.kanshasai.backend.infra.redis.event.RedisEvent

@Service
class ParticipantAnswerService(
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
    private val redisEventService: RedisEventService,
    private val participantRepository: ParticipantRepository,
    private val participantAnswerRepository: ParticipantAnswerRepository,
    private val s3Service: S3Service,
) {
    fun getAnswer(participantId: UlidId, quizId: UlidId) = runCatching {
        val participant = participantRepository.findById(participantId).getOrThrowService()
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val session = sessionRepository.findById(participant.sessionId).getOrThrowService()

        participantAnswerRepository.find(session, quiz, participant).getOrThrowService()
    }
    fun setAnswer(participantId: UlidId, quizId: UlidId, answer: String, time: Float) {
        val participant = participantRepository.findById(participantId).getOrThrowService()
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val session = sessionRepository.findById(participant.sessionId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrowService()

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

        val participantList = participantRepository.listBySession(session).getOrThrowService()
        val participantAnswerList = participantAnswerRepository.listBySessionQuiz(sessionQuiz).getOrThrowService()

        val participantMap = participantList.map { p ->
            p to participantAnswerList.find { it.participantId == p.id }
        }

        redisEventService.publish(
            RedisEvent.UpdateParticipant(
                participantMap.map {
                    ParticipantRedisEntity(
                        participantId = it.first.id.toString(),
                        name = it.first.name,
                        imageUrl = s3Service.generatePresignedUrl(it.first.imageId),
                        participantType = it.first.type.toGrpcType(),
                        connected = it.first.isConnected,
                        isAnswered = it.second != null,
                    )
                },
                session.id.toString(),
            ),
        )
    }
}
