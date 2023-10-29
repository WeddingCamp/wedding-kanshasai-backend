package wedding.kanshasai.backend.domain.state

interface RankStateMachineInterface {
    fun next(): Result<RankStateMachineInterface>
    fun back(): Result<RankStateMachineInterface>

    val value: Int
    val index: Int get() = value - 1
}
