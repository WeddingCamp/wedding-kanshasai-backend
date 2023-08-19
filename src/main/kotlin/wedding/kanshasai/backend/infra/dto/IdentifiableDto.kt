package wedding.kanshasai.backend.infra.dto

import wedding.kanshasai.backend.infra.dto.identifier.DtoIdentifier

abstract class IdentifiableDto<T : DtoIdentifier>(
    open var identifier: T,
) {
    abstract fun strictEquals(other: Any?): Boolean
}
