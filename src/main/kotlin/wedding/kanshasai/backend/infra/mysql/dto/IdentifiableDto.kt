package wedding.kanshasai.backend.infra.mysql.dto

import wedding.kanshasai.backend.infra.mysql.dto.identifier.DtoIdentifier

abstract class IdentifiableDto<T : DtoIdentifier>(
    open var isDeleted: Boolean,
) {
    abstract var identifier: T
}
