package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.tribeservice.groups.GroupServiceRequests
import com.scrumdapp.tribeservice.tribes.dto.TribeCreateDto
import com.scrumdapp.tribeservice.tribes.dto.TribeOverviewResponseDto
import com.scrumdapp.tribeservice.tribes.dto.TribePatchDto
import com.scrumdapp.tribeservice.tribes.dto.TribeResponseDto
import com.scrumdapp.tribeservice.tribes.dto.TribeGroupResponse
import com.scrumdapp.tribeservice.tribes.models.Tribe
import com.scrumdapp.tribeservice.tribes.models.TribeCoach
import org.springframework.stereotype.Service

@Service
class TribeService(
    private val tribeRepository: TribeRepository,
    private val groupServiceRequests: GroupServiceRequests,
    private val tribeGroupRepository: TribeGroupRepository,
    private val tribeCoachRepository: TribeCoachRepository
) {

    fun getTribes(userId: Long): List<TribeOverviewResponseDto> {
        val tribeGroups = tribeGroupRepository.findByUserId(userId)

        return tribeGroups
            .distinctBy { it.tribe }
            .map { it.tribe }
            .map { tribe -> tribe.toOverviewResponse(tribeGroups.count { it.tribe.id == tribe.id}) }
    }


    fun createTribe(dto: TribeCreateDto, userId: Long): TribeResponseDto {
        val tribe = Tribe().apply {
            this.ownerId = userId
            this.name = dto.name
        }
        val savedTribe = tribeRepository.save(tribe)

        val coach = TribeCoach().apply {
            this.userId = userId
            this.tribe = savedTribe
        }
        tribeCoachRepository.save(coach)

        return savedTribe.toResponse(emptyList())
    }

    fun getTribeById(tribeId: Long): TribeResponseDto {
        val tribe = tribeRepository.getTribeById(tribeId) ?: throw RuntimeException("Not found")
        val tribeGroups = getTribeGroups(tribeId)

        return tribe.toResponse(tribeGroups)
    }

    fun updateTribe(tribeId: Long, dto: TribePatchDto): TribeResponseDto {
        val tribe = tribeRepository.getTribeById(tribeId) ?: throw RuntimeException("Not found")
        val tribeGroups = getTribeGroups(tribeId)

        val patchedTribe = tribe.patch(dto)
        return patchedTribe.toResponse(tribeGroups)
    }

    private fun getTribeGroups(tribeId: Long): List<TribeGroupResponse> {
        val tribeGroups = tribeGroupRepository.findByTribeId(tribeId)
        val userGroups = groupServiceRequests.getGroups()

        val groups = mutableListOf<TribeGroupResponse>()
        for (tribeGroup in tribeGroups) {
            val group = userGroups.find { it.id == tribeGroup.groupId } ?: continue
            val item = TribeGroupResponse(
                tribeGroup.groupId,
                group.name,
                group.memberCount
            )
            groups.add(item)
        }
        return groups
    }
}

