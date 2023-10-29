package wedding.kanshasai.backend.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import wedding.kanshasai.backend.domain.entity.*
import wedding.kanshasai.backend.domain.exception.InvalidStateException
import wedding.kanshasai.backend.domain.manager.ResultStateManager
import wedding.kanshasai.backend.domain.state.*
import wedding.kanshasai.backend.domain.value.*
import wedding.kanshasai.backend.infra.mysql.repository.*
import wedding.kanshasai.backend.infra.redis.entity.*
import wedding.kanshasai.backend.infra.redis.event.*
import wedding.kanshasai.v1.ResultPresentType
import wedding.kanshasai.v1.ResultRankingType
import wedding.kanshasai.v1.ResultTitleType
import java.sql.Timestamp
import java.util.Date

private val logger = KotlinLogging.logger {}

@Service
class SessionService(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionQuizRepository: SessionQuizRepository,
    private val redisEventService: RedisEventService,
    private val choiceRepository: ChoiceRepository,
    private val participantRepository: ParticipantRepository,
    private val participantAnswerRepository: ParticipantAnswerRepository,
) {
    companion object {
        const val MAX_INTRODUCTION_ID = 11
    }
    fun createSession(eventId: UlidId, name: String): Session {
        val event = eventRepository.findById(eventId).getOrThrowService()
        val quizList = quizRepository.listByEvent(event).getOrThrowService()
        val session = sessionRepository.createSession(event, name).getOrThrowService()
        sessionQuizRepository.insertQuizList(session, quizList).getOrThrowService()
        return session
    }

    fun findById(sessionId: UlidId): Session {
        return sessionRepository.findById(sessionId).getOrThrowService()
    }

    fun listSessions(eventId: UlidId, includeFinished: Boolean): List<Session> {
        val event = eventRepository.findById(eventId).getOrThrowService()
        return sessionRepository.listByEvent(event, includeFinished).getOrThrowService()
    }

    fun setCoverScreen(sessionId: UlidId, isVisible: Boolean) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        sessionRepository.update(session.clone().apply { isCoverVisible = isVisible }).getOrThrowService()
        redisEventService.publish(RedisEvent.Cover(isVisible, sessionId.toString()))
    }

    fun getSessionState(sessionId: UlidId): SessionState {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        return sessionRepository.findById(session.id).getOrThrowService().state
    }

    fun nextIntroduction(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.INTRODUCTION) {
            throw InvalidStateException("Session state is not INTRODUCTION.")
        }

        val introductionId = session.currentIntroductionId + 1

        if (introductionId > MAX_INTRODUCTION_ID) {
            throw InvalidStateException("Introduction is already last.")
        }

        sessionRepository.update(session.clone().apply { currentIntroductionId = introductionId }).getOrThrowService()
        redisEventService.publish(
            RedisEvent.Introduction(
                introductionId,
                introductionId <= 1,
                introductionId >= MAX_INTRODUCTION_ID,
                sessionId.toString(),
            ),
        )
    }

    fun backIntroduction(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.INTRODUCTION) {
            throw InvalidStateException("Session state is not INTRODUCTION.")
        }

        val introductionId = session.currentIntroductionId - 1

        if (introductionId < 1) {
            throw InvalidStateException("Introduction is already first.")
        }

        sessionRepository.update(session.clone().apply { currentIntroductionId = introductionId }).getOrThrowService()
        redisEventService.publish(
            RedisEvent.Introduction(
                introductionId,
                introductionId <= 1,
                introductionId >= MAX_INTRODUCTION_ID,
                sessionId.toString(),
            ),
        )
    }

    fun finishIntroduction(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
            },
        ).getOrThrowService()

        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun closeQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.QUIZ_PLAYING) {
            throw InvalidStateException("Session state is not QUIZ_PLAYING.")
        }
        val quizId = session.currentQuizId ?: throw InvalidStateException("Current quiz is not set.")
        val nextState = session.state.next(SessionState.QUIZ_CLOSED).getOrThrowService()
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val choiceList = choiceRepository.listByQuiz(quiz).getOrThrowService()

        sessionRepository.update(session.clone().apply { state = nextState }).getOrThrowService()

        val quizNumber = sessionQuizRepository.listBySession(session).getOrThrowService()
            .count { it.second.isCompleted } + 1

        redisEventService.publish(
            RedisEvent.QuizTimeUp(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), it.body)
                },
                quizNumber,
                session.id.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun setNextQuiz(sessionId: UlidId, quizId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrThrowService()

        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrowService()

        if (sessionQuiz.isCompleted) {
            throw InvalidStateException("Quiz is already completed. (quizId=$quizId)")
        }

        val choiceList = choiceRepository.listByQuiz(quiz).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                currentQuizId = quiz.id
            },
        ).getOrThrowService()

        val quizNumber = sessionQuizRepository.listBySession(session).getOrThrowService()
            .count { it.second.isCompleted } + 1

        redisEventService.publish(
            RedisEvent.PreQuiz(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), redisEventService.parseBody(it.body, quiz.type))
                },
                quizNumber,
                session.id.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun showQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_SHOWING).getOrThrowService()
        val (quiz, choiceList) = session.getCurrentQuiz()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
            },
        ).getOrThrowService()

        val quizNumber = sessionQuizRepository.listBySession(session).getOrThrowService()
            .count { it.second.isCompleted } + 1

        redisEventService.publish(
            RedisEvent.ShowQuiz(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), redisEventService.parseBody(it.body, quiz.type))
                },
                quizNumber,
                session.id.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun startQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(SessionState.QUIZ_PLAYING).getOrThrowService()
        val (quiz, choiceList, sessionQuiz) = session.getCurrentQuiz()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
            },
        ).getOrThrowService()

        sessionQuizRepository.update(
            sessionQuiz.clone().apply {
                startedAt = Timestamp.from(Date().toInstant())
            },
        ).getOrThrowService()

        val quizNumber = sessionQuizRepository.listBySession(session).getOrThrowService()
            .count { it.second.isCompleted } + 1

        redisEventService.publish(
            RedisEvent.StartQuiz(
                quiz.id.toString(),
                quiz.body,
                quiz.type.toGrpcType(),
                choiceList.map {
                    QuizChoiceRedisEntity(it.id.toString(), redisEventService.parseBody(it.body, quiz.type))
                },
                quizNumber,
                sessionId.toString(),
            ),
        )
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun showQuizResult(sessionId: UlidId, quizResultType: QuizResultType) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val currentState = session.state
        val nextState = session.state.next(SessionState.QUIZ_RESULT).getOrThrowService()
        val (quiz, choiceList, sessionQuiz) = session.getCurrentQuiz()

        val redisEvent = when (quizResultType) {
            QuizResultType.VOTE_LIST -> {
                val quizNumber = sessionQuizRepository.listBySession(session).getOrThrowService()
                    .count { it.second.isCompleted } + 1

                val participantAnswerList = participantAnswerRepository.listBySessionQuiz(sessionQuiz).getOrThrowService()

                RedisEvent.QuizAnswerList(
                    quiz.id.toString(),
                    quiz.body,
                    quiz.type.toGrpcType(),
                    choiceList.map {
                        val choiceId = it.id.toString()
                        QuizChoiceWithCountRedisEntity(
                            choiceId,
                            redisEventService.parseBody(it.body, quiz.type),
                            participantAnswerList.count { pa -> pa.answer == choiceId },
                        )
                    },
                    quizNumber,
                    session.id.toString(),
                )
            }

            QuizResultType.RESULT -> {
                sessionRepository.update(
                    session.clone().apply {
                        state = nextState
                        currentQuizResult = quizResultType
                    },
                ).getOrThrowService()

                checkResult(quiz, sessionQuiz)
                val participantAnswerList = participantAnswerRepository.listBySessionQuiz(sessionQuiz).getOrThrowService()

                val quizNumber = sessionQuizRepository.listBySession(session).getOrThrowService()
                    .count { it.second.isCompleted }

                redisEventService.publishState(currentState, nextState, session.id)
                RedisEvent.QuizResult(
                    quiz.id.toString(),
                    quiz.body,
                    quiz.type.toGrpcType(),
                    choiceList.map {
                        val choiceId = it.id.toString()
                        QuizChoiceWithResultRedisEntity(
                            choiceId,
                            redisEventService.parseBody(it.body, quiz.type),
                            participantAnswerList.count { pa -> pa.answer == choiceId },
                            quiz.getCorrectAnswer().choiceIdList.contains(it.id.toString()),
                        )
                    },
                    quizNumber,
                    session.id.toString(),
                )
            }

            QuizResultType.FASTEST_RANKING -> {
                sessionRepository.update(
                    session.clone().apply {
                        state = nextState
                        currentQuizResult = quizResultType
                    },
                ).getOrThrowService()

                checkResult(quiz, sessionQuiz)
                val participantAnswerList = participantAnswerRepository.listBySessionQuiz(sessionQuiz).getOrThrowService()

                redisEventService.publishState(currentState, nextState, session.id)
                RedisEvent.QuizSpeedRanking(
                    participantAnswerList
                        .filter { it.isCorrect }
                        .sortedBy { it.time }
                        .mapIndexed { index, it ->
                            val participant = participantRepository.findById(it.participantId).getOrThrowService()
                            ParticipantQuizTimeRedisEntity(
                                participant.name,
                                participant.imageId.toString(),
                                it.time,
                                index + 1,
                            )
                        },
                    session.id.toString(),
                )
            }

            else -> {
                throw InvalidStateException("Invalid QuizResultType. (quizResultType=$quizResultType)")
            }
        }
        redisEventService.publish(redisEvent)
    }

    private fun checkResult(quiz: Quiz, sessionQuiz: SessionQuiz) {
        logger.info { "IS_COMPLETED: " + sessionQuiz.isCompleted }
        if(sessionQuiz.isCompleted) return

        sessionQuizRepository.update(sessionQuiz.clone().apply { isCompleted = true }).getOrThrowService()

        val participantAnswerList = participantAnswerRepository.listBySessionQuiz(sessionQuiz).getOrThrowService()
        participantAnswerList.forEach {
            participantAnswerRepository.update(
                it.clone().apply {
                    isCorrect = when (quiz.type) {
                        QuizType.FOUR_CHOICES_QUIZ -> quiz.getCorrectAnswer().choiceIdList.contains(it.answer)
                        QuizType.SORT_IMAGE_QUIZ -> quiz.getCorrectAnswer().choiceIdList.contains(it.answer)
                        QuizType.REALTIME_FOUR_CHOICE_QUIZ -> sessionQuiz.getCorrectAnswer().choiceIdList.contains(it.answer)
                        else -> quiz.correctAnswer == it.answer
                    }
                },
            )
        }
    }

    fun cancelCurrentQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        if (!listOf(SessionState.QUIZ_SHOWING, SessionState.QUIZ_PLAYING, SessionState.QUIZ_CLOSED).contains(session.state)) {
            throw InvalidStateException("Session state is not QUIZ_SHOWING or QUIZ_PLAYING or QUIZ_CLOSED.")
        }
        val nextState = SessionState.QUIZ_WAITING
        val quizId = session.currentQuizId ?: throw InvalidStateException("Current quiz is not set.")
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                currentQuizId = null
            },
        ).getOrThrowService()

        participantAnswerRepository.deleteBySessionQuiz(sessionQuiz).getOrThrowService()

        redisEventService.publish(RedisEvent.CancelQuiz(sessionId.toString()))
        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun startSessionResult(sessionId: UlidId, resultType: ResultType) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val nextState = session.state.next(resultType.sessionState).getOrThrowService()

        val resultList = participantRepository
            .listBySessionWithResult(session)
            .getOrThrowService()

        val resultStateMachine = ResultStateMachine.of(ResultState.RANKING_NORMAL)
        val resultStateManager = ResultStateManager.of(
            resultStateMachine,
            ResultRankStateMachine.of(ResultRankState.RANK, resultStateMachine.value.rankStateList),
            RankStateMachine.of(resultList.size + 1),
        )

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                resultState = resultStateManager
            },
        ).getOrThrowService()

        redisEventService.publish(RedisEvent.ShowResultTitle(resultType.value.grpcValue, sessionId.toString()))
        redisEventService.publishState(session.state, nextState, session.id)
    }

    enum class RankType { NORMAL, BOOBY, JUST }

    /**
     * {fromIndex}から{length}件取得する関数
     * {fromIndex}がリストのサイズを超えている場合は、切り落とされる
     */
    fun <T> List<T>.safeSubList(fromIndex: Int, length: Int): List<T> {
        return subList(fromIndex, (fromIndex + length).coerceAtMost(size))
    }

    fun nextSessionResult(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        val redisEvent = when (session.state) {
            SessionState.INTERIM_ANNOUNCEMENT -> {
                val newRankState = InterimRankStateMachine.of(session.resultState.rankStateMachine.value).next()
                    .getOrElse {
                        // 結果発表終了
                        val nextState = SessionState.QUIZ_WAITING
                        sessionRepository.update(
                            session.clone().apply {
                                state = nextState
                            },
                        ).getOrThrowService()
                        redisEventService.publishState(session.state, nextState, session.id)
                        return
                    }

                sessionRepository.update(
                    session.clone().apply {
                        resultState = ResultStateManager.of(
                            session.resultState.resultStateMachine,
                            session.resultState.resultRankStateMachine,
                            newRankState,
                        )
                    },
                ).getOrThrowService()

                // スコア一覧を取得し、表示したい10件を取得する
                val resultList = participantRepository
                    .listBySessionWithResult(session)
                    .getOrThrowService()
                    .sortedBy { it.second.rank }
                    .safeSubList(newRankState.index, 10)

                RedisEvent.ShowResultRanking(
                    resultList.map {
                        ParticipantSessionScoreRedisEntity(
                            it.first.name,
                            it.second.score,
                            false,
                            it.second.rank,
                            it.second.time,
                        )
                    },
                    0,
                    resultList.size,
                    ResultRankingType.RESULT_RANKING_TYPE_RANK,
                    newRankState.value != 1,
                    sessionId.toString(),
                )
            }
            SessionState.FINAL_RESULT_ANNOUNCEMENT -> {
                val newResultState = session.resultState.next().getOrElse {
                    // 結果発表終了
                    val nextState = SessionState.FINISHED
                    sessionRepository.update(
                        session.clone().apply {
                            state = nextState
                        },
                    ).getOrThrowService()
                    redisEventService.publish(RedisEvent.Finish(sessionId.toString()))
                    redisEventService.publishState(session.state, nextState, session.id)
                    return
                }

                sessionRepository.update(
                    session.clone().apply {
                        resultState = newResultState
                    },
                ).getOrThrowService()

                when (newResultState.resultRankStateMachine.value) {
                    ResultRankState.RANK, ResultRankState.PRE_RANK -> {
                        // スコア一覧を取得し、表示したい10件を取得する
                        val resultList = participantRepository
                            .listBySessionWithResult(session)
                            .getOrThrowService()
                            .sortedBy { it.second.rank }

                        val resultListWithType = resultList.map {
                            Triple(
                                it.first,
                                it.second,
                                when (it.second.rank) {
                                    resultList.size - 1 -> RankType.BOOBY
                                    48 -> RankType.JUST
                                    else -> RankType.NORMAL
                                },
                            )
                        }

                        val sortedResultList = when (newResultState.resultStateMachine.value) {
                            ResultState.RANKING_NORMAL -> {
                                resultListWithType
                                   .safeSubList(newResultState.rankStateMachine.index, 10)
                                   .map {
                                       if (it.third == RankType.BOOBY || it.third == RankType.JUST) {
                                           it.first.name = "？？？"
                                       }
                                       it
                                   }
                            }
                            ResultState.RANKING_BOOBY -> resultListWithType.filter { it.third == RankType.BOOBY }
                            ResultState.RANKING_JUST -> resultListWithType.filter { it.third == RankType.JUST }
                            else -> resultListWithType.safeSubList(0, 10)
                        }

                        // 予め表示しておく数は以下に定義する
                        val preDisplayCount = when (newResultState.resultStateMachine.value) {
                            ResultState.RANKING_NORMAL -> 0
                            ResultState.RANKING_TOP_8 -> 0
                            ResultState.RANKING_TOP_7 -> sortedResultList.size - 7
                            ResultState.RANKING_TOP_6 -> sortedResultList.size - 6
                            ResultState.RANKING_TOP_5 -> sortedResultList.size - 5
                            ResultState.RANKING_TOP_4 -> sortedResultList.size - 4
                            ResultState.RANKING_TOP_3 -> sortedResultList.size - 3
                            ResultState.RANKING_TOP_2 -> sortedResultList.size - 2
                            ResultState.RANKING_TOP_1 -> sortedResultList.size - 1
                            ResultState.RANKING_BOOBY -> 0
                            ResultState.RANKING_JUST -> 0
                            else -> throw InvalidStateException("Invalid ResultState.")
                        }

                        // アニメーションで表示したい数はPRE_RANKの場合はpreDisplayCountと同等
                        // それ以外の場合はResultStateで切り分ける
                        val displayCount = if (newResultState.resultRankStateMachine.value == ResultRankState.PRE_RANK) {
                            preDisplayCount
                        } else {
                            when (newResultState.resultStateMachine.value) {
                                ResultState.RANKING_NORMAL -> sortedResultList.size
                                ResultState.RANKING_TOP_8 -> sortedResultList.size - 7
                                else -> preDisplayCount + 1
                            }
                        }

                        val resultRankingType = when (newResultState.resultStateMachine.value) {
                            ResultState.RANKING_BOOBY -> ResultRankingType.RESULT_RANKING_TYPE_BOOBY
                            ResultState.RANKING_JUST -> ResultRankingType.RESULT_RANKING_TYPE_JUST
                            else -> ResultRankingType.RESULT_RANKING_TYPE_RANK
                        }

                        val resultState = newResultState.resultStateMachine.value
                        RedisEvent.ShowResultRanking(
                            sortedResultList.map {
                                val isEmphasis = when {
                                    resultState == ResultState.RANKING_TOP_1 && it.second.rank == 1 -> true
                                    resultState == ResultState.RANKING_TOP_2 && it.second.rank == 2 -> true
                                    resultState == ResultState.RANKING_TOP_3 && it.second.rank == 3 -> true
                                    resultState == ResultState.RANKING_TOP_4 && it.second.rank == 4 -> true
                                    resultState == ResultState.RANKING_TOP_5 && it.second.rank == 5 -> true
                                    resultState == ResultState.RANKING_TOP_6 && it.second.rank == 6 -> true
                                    resultState == ResultState.RANKING_TOP_7 && it.second.rank == 7 -> true
                                    // NOTE: 8位は7位のPRE_RANKという側面があるので7位と同等に扱う
                                    resultState == ResultState.RANKING_TOP_8 && it.second.rank == 7 -> true
                                    else -> false
                                }
                                ParticipantSessionScoreRedisEntity(
                                    it.first.name,
                                    it.second.score,
                                    isEmphasis,
                                    it.second.rank,
                                    it.second.time,
                                )
                            },
                            preDisplayCount,
                            displayCount,
                            resultRankingType,
                            !(
                                newResultState.resultStateMachine.value == ResultState.RANKING_TOP_1 &&
                                    newResultState.resultRankStateMachine.value == ResultRankState.PRESENT
                                ),
                            sessionId.toString(),
                        )
                    }
                    ResultRankState.PRESENT -> {
                        val resultPresentType = when (newResultState.resultStateMachine.value) {
                            ResultState.RANKING_BOOBY -> ResultPresentType.RESULT_PRESENT_TYPE_BOOBY
                            ResultState.RANKING_JUST -> ResultPresentType.RESULT_PRESENT_TYPE_JUST
                            ResultState.RANKING_TOP_4_7_PRESENT -> ResultPresentType.RESULT_PRESENT_TYPE_4_7
                            else -> ResultPresentType.RESULT_PRESENT_TYPE_RANK
                        }
                        RedisEvent.ShowResultPresent(
                            newResultState.rank,
                            resultPresentType,
                            sessionId.toString(),
                        )
                    }
                    ResultRankState.TITLE, ResultRankState.DUMMY_TITLE, ResultRankState.DUMMY_TITLE_MESSAGE -> {
                        val resultTitleType = when (newResultState.resultRankStateMachine.value) {
                            ResultRankState.DUMMY_TITLE -> {
                                // ダミータイトルは常にRANK
                                ResultTitleType.RESULT_TITLE_TYPE_RANK
                            }
                            ResultRankState.DUMMY_TITLE_MESSAGE -> {
                                when (newResultState.resultStateMachine.value) {
                                    ResultState.RANKING_BOOBY -> ResultTitleType.RESULT_TITLE_TYPE_RANK_DUMMY_1
                                    ResultState.RANKING_JUST -> ResultTitleType.RESULT_TITLE_TYPE_RANK_DUMMY_2
                                    else -> ResultTitleType.RESULT_TITLE_TYPE_RANK
                                }
                            }
                            ResultRankState.TITLE -> {
                                when (newResultState.resultStateMachine.value) {
                                    ResultState.RANKING_BOOBY -> ResultTitleType.RESULT_TITLE_TYPE_BOOBY
                                    ResultState.RANKING_JUST -> ResultTitleType.RESULT_TITLE_TYPE_JUST
                                    ResultState.RANKING_TOP_3 -> ResultTitleType.RESULT_TITLE_TYPE_RANK_ACTUAL
                                    else -> ResultTitleType.RESULT_TITLE_TYPE_RANK
                                }
                            }
                            else -> throw InvalidStateException("Invalid ResultRankState.")
                        }

                        RedisEvent.ShowResultRankingTitle(
                            newResultState.rank,
                            resultTitleType,
                            sessionId.toString(),
                        )
                    }
                }
            }
            else -> {
                throw InvalidStateException("Session state is not INTERIM_ANNOUNCEMENT or FINAL_RESULT_ANNOUNCEMENT.")
            }
        }

        redisEventService.publish(redisEvent)
    }

    fun finishQuiz(sessionId: UlidId) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()

        if (session.state != SessionState.QUIZ_RESULT) {
            throw InvalidStateException("Session state is not QUIZ_RESULT.")
        }

        val nextState = session.state.next(SessionState.QUIZ_WAITING).getOrThrowService()

        sessionRepository.update(
            session.clone().apply {
                state = nextState
                currentQuizId = null
                currentQuizResult = null
            },
        ).getOrThrowService()

        redisEventService.publishState(session.state, nextState, session.id)
    }

    fun setQuizAnswer(sessionId: UlidId, quizId: UlidId, answer: String) {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(session, quiz).getOrThrowService()

        sessionQuizRepository.update(
            sessionQuiz.clone().also {
                it.sessionQuizCorrectAnswer = answer
            },
        ).getOrThrowService()
    }

    fun getCurrentQuiz(sessionId: UlidId): Result<Triple<Quiz, List<Choice>, SessionQuiz>> = runCatching {
        val session = sessionRepository.findById(sessionId).getOrThrowService()
        session.getCurrentQuiz()
    }

    fun Session.getCurrentQuiz(): Triple<Quiz, List<Choice>, SessionQuiz> {
        val quizId = this.currentQuizId
        if (quizId == null) {
            throw InvalidStateException("Current quiz is not set.")
        }
        val quiz = quizRepository.findById(quizId).getOrThrowService()
        val sessionQuiz = sessionQuizRepository.find(this, quiz).getOrThrowService()
        val choiceList = choiceRepository.listByQuiz(quiz).getOrThrowService()
        return Triple(quiz, choiceList, sessionQuiz)
    }
}
