package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.DtoIdentifier

abstract class IdentifiableDto<T : DtoIdentifier>(
    open var identifier: T,
    open var isDeleted: Boolean,
) {
    abstract fun strictEquals(other: Any?): Boolean
}
