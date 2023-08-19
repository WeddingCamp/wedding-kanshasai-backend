package wedding.kanshasai.backend.infra.dto

abstract class IdentifiableDto(
    open var id: ByteArray = byteArrayOf(),
) {
    abstract fun strictEquals(other: Any?): Boolean
}
