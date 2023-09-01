package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.mysql.dto.ParticipantDto
import wedding.kanshasai.backend.infra.mysql.dto.SessionDto
import wedding.kanshasai.backend.infra.mysql.dto.identifier.StandardIdentifier
import wedding.kanshasai.backend.infra.mysql.mapper.ParticipantMapper

@WeddingKanshasaiSpringBootTest
class ParticipantMapperTests : MapperCRUDTest<ParticipantMapper, StandardIdentifier, ParticipantDto>() {

    @Autowired
    lateinit var testTool: MapperTestTool

    override fun stubDtoList(): Pair<List<ParticipantDto>, List<ParticipantDto>> {
        val participantDtoList = mutableListOf<ParticipantDto>()
        val updateParticipantDtoList = mutableListOf<ParticipantDto>()

        val sessionDtoList = mutableListOf<SessionDto>()

        repeat(10) {
            val eventDto = testTool.createAndInsertEventDto()
            repeat(3) {
                val sessionDto = testTool.createAndInsertSessionDto(eventDto)
                sessionDtoList.add(sessionDto)
                repeat(3) {
                    val participantDto = testTool.createParticipantDto(sessionDto)
                    participantDtoList.add(participantDto)
                    updateParticipantDtoList.add(
                        participantDto.copy().apply {
                            name = testTool.uuid
                            imageId = testTool.maybeNull(UlidId.new().toByteArray())
                            isDeleted = testTool.trueOrFalse
                            sessionId = sessionDtoList.random().sessionIdentifier.id
                        },
                    )
                }
            }
        }
        return Pair(participantDtoList, updateParticipantDtoList)
    }
}
