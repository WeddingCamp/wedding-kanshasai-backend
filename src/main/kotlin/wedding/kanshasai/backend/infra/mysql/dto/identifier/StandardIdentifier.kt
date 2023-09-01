package wedding.kanshasai.backend.infra.mysql.dto.identifier

import wedding.kanshasai.backend.domain.value.UlidId

data class StandardIdentifier(
    var id: ByteArray = byteArrayOf(),
) : DtoIdentifier {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardIdentifier

        return id.contentEquals(other.id)
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(id='${UlidId.of(id)}')"
    }
}
