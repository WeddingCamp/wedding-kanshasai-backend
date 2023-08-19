package wedding.kanshasai.backend.infra.mapper

import org.springframework.beans.factory.annotation.Autowired
import wedding.kanshasai.backend.WeddingKanshasaiSpringBootTest
import wedding.kanshasai.backend.domain.value.UlidId
import wedding.kanshasai.backend.infra.MapperTestTool
import wedding.kanshasai.backend.infra.dto.ParticipantDto
import wedding.kanshasai.backend.infra.dto.SessionDto
import wedding.kanshasai.backend.infra.dto.identifier.StandardIdentifier

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
                            session = sessionDtoList.random()
                            sessionId = session!!.identifier.id
                        },
                    )
                }
            }
        }
        return Pair(participantDtoList, updateParticipantDtoList)
    }
}
