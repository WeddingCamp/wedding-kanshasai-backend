package wedding.kanshasai.backend.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import wedding.kanshasai.backend.entity.Event

@Mapper
interface EventMapper {
    @Select(
        """
        SELECT
            *
        FROM
            events
        WHERE
            id = #{id}
    """,
    )
    fun findById(
        @Param("id") id: ByteArray,
    ): Event?

    @Insert(
        """
        INSERT INTO events(
            id,
            name
        )
        VALUES (
            #{event.id},
            #{event.name}
        )
    """,
    )
    fun createEvent(
        @Param("event") event: Event,
    )

    @Select(
        """
        SELECT
            *
        FROM
            events
        WHERE
            is_deleted = false
            OR is_deleted = #{includeDeleted}
    """,
    )
    fun listEvents(
        @Param("includeDeleted") includeDeleted: Boolean = false,
    ): List<Event>

    @Update(
        """
        UPDATE
            events
        SET
            is_deleted = true
        WHERE
            id = #{id}
    """,
    )
    fun deleteById(
        @Param("id") id: ByteArray,
    ): Boolean
}
