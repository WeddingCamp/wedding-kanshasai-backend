package wedding.kanshasai.backend.infra.mapper

import org.apache.ibatis.annotations.Mapper

@Mapper
interface TruncateMapper {
    fun truncate(): Boolean
}
