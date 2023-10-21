package wedding.kanshasai.backend.domain.manager

import io.github.oshai.kotlinlogging.KotlinLogging
import wedding.kanshasai.backend.domain.state.RankStateMachine
import wedding.kanshasai.backend.domain.state.ResultRankStateMachine
import wedding.kanshasai.backend.domain.state.ResultStateMachine
import wedding.kanshasai.backend.domain.value.ResultState

private val logger = KotlinLogging.logger {}

class ResultStateManager private constructor(
    val resultStateMachine: ResultStateMachine,
    val rankStateMachine: RankStateMachine,
    val resultRankStateMachine: ResultRankStateMachine,
) {
    fun next() = runCatching {
        var newResultStateMachine = resultStateMachine
        var newRankStateMachine = rankStateMachine
        var newResultRankStateMachine = resultRankStateMachine

        // ResultStateがNORMALの時は、RankStateで制御する
        if (resultStateMachine.value == ResultState.RANKING_NORMAL) {
            newRankStateMachine = rankStateMachine.next().getOrThrow()
            // RankStateが8以下になったら、ResultStateを進める
            if (newRankStateMachine.value <= 8) {
                newResultStateMachine = resultStateMachine.next().getOrThrow()
                newResultRankStateMachine = ResultRankStateMachine.of(newResultStateMachine.value.rankStateList)
            }
            // 以後RankStateは意識しない
            return@runCatching ResultStateManager(newResultStateMachine, newRankStateMachine, newResultRankStateMachine)
        }

        // ResultRankStateを進める
        newResultRankStateMachine = resultRankStateMachine.next().getOrElse {
            // ResultRankStateが最後だったら、ResultStateを進める
            newResultStateMachine = resultStateMachine.next().getOrThrow()
            ResultRankStateMachine.of(newResultStateMachine.value.rankStateList)
        }

        ResultStateManager(newResultStateMachine, newRankStateMachine, newResultRankStateMachine)
    }

    fun back() = runCatching {
        var newResultStateMachine = resultStateMachine
        var newRankStateMachine = rankStateMachine
        var newResultRankStateMachine = resultRankStateMachine

        // ResultStateがTOP_8の時は、Rankを11に戻す
        if (resultStateMachine.value == ResultState.RANKING_TOP_8) {
            newRankStateMachine = RankStateMachine.of(11)
            newResultStateMachine = resultStateMachine.back().getOrThrow()
            return@runCatching ResultStateManager(newResultStateMachine, newRankStateMachine, newResultRankStateMachine)
        }

        // ResultStateがNORMALの時は、Rankを10巻き戻す
        if (resultStateMachine.value == ResultState.RANKING_NORMAL) {
            newRankStateMachine = rankStateMachine.back().getOrThrow()
            return@runCatching ResultStateManager(newResultStateMachine, newRankStateMachine, newResultRankStateMachine)
        }

        // ResultRankStateが最初じゃない時は、ResultRankStateを最初に戻す
        if (resultRankStateMachine.value != resultStateMachine.value.rankStateList.first()) {
            newResultRankStateMachine = ResultRankStateMachine.of(newResultStateMachine.value.rankStateList)
            return@runCatching ResultStateManager(newResultStateMachine, newRankStateMachine, newResultRankStateMachine)
        }

        newResultStateMachine = resultStateMachine.back().getOrThrow()
        newResultRankStateMachine = ResultRankStateMachine.of(newResultStateMachine.value.rankStateList)

        ResultStateManager(newResultStateMachine, newRankStateMachine, newResultRankStateMachine)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ResultStateManager) return false
        if (resultStateMachine != other.resultStateMachine) return false
        if (resultRankStateMachine != other.resultRankStateMachine) return false
        if (rankStateMachine != other.rankStateMachine) return false
        return true
    }

    override fun toString(): String {
        return "ResultStateManager(" +
            "resultStateMachine=$resultStateMachine, " +
            "rankStateMachine=$rankStateMachine, " +
            "resultRankStateMachine=$resultRankStateMachine)"
    }

    override fun hashCode(): Int {
        var result = resultStateMachine.hashCode()
        result = 31 * result + rankStateMachine.hashCode()
        result = 31 * result + resultRankStateMachine.hashCode()
        return result
    }

    companion object {
        fun of(
            resultStateMachine: ResultStateMachine,
            resultRankStateMachine: ResultRankStateMachine,
            rankStateMachine: RankStateMachine,
        ): ResultStateManager {
            var newResultStateMachine = resultStateMachine
            var newResultRankStateMachine = resultRankStateMachine

            // ResultStateがNORMALでRankが8以下の場合は、ResultStateを補正する
            // これでResultStateとRankStateの整合性が取れる
            if (resultStateMachine.value == ResultState.RANKING_NORMAL && rankStateMachine.value <= 8) {
                logger.warn { "ResultState has been corrected to the appropriate ResultState based on RankState." }
                newResultStateMachine = when (rankStateMachine.value) {
                    8 -> ResultStateMachine.of(ResultState.RANKING_TOP_8)
                    7 -> ResultStateMachine.of(ResultState.RANKING_TOP_7)
                    6 -> ResultStateMachine.of(ResultState.RANKING_TOP_6)
                    5 -> ResultStateMachine.of(ResultState.RANKING_TOP_5)
                    4 -> ResultStateMachine.of(ResultState.RANKING_TOP_4)
                    3 -> ResultStateMachine.of(ResultState.RANKING_BOOBY)
                    2 -> ResultStateMachine.of(ResultState.RANKING_TOP_2)
                    1 -> ResultStateMachine.of(ResultState.RANKING_TOP_1)
                    else -> throw IllegalArgumentException("RankState must be greater than or equal to 1.")
                }
            }

            // ResultRankStateがResultStateが定義しているRankStateListに含まれていない場合は、ResultRankStateを初期化する
            // これでResultStateとResultRankStateの整合性が取れる
            if (!resultStateMachine.value.rankStateList.contains(resultRankStateMachine.value)) {
                val target = resultStateMachine.value.rankStateList.first()
                logger.warn {
                    "rankState(${resultRankStateMachine.value}) must be in rankStateList(${resultStateMachine.value.rankStateList.joinTo(
                        StringBuffer(),
                        ",",
                    )}). rankState has been corrected to $target."
                }
                newResultRankStateMachine = ResultRankStateMachine.of(target, resultStateMachine.value.rankStateList)
            }

            return ResultStateManager(newResultStateMachine, rankStateMachine, newResultRankStateMachine)
        }
    }
}