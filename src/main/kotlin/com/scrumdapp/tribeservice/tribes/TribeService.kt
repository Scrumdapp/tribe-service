package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.tribeservice.groups.GroupServiceRequests
import com.scrumdapp.tribeservice.tribes.dto.TribeCreateDto
import com.scrumdapp.tribeservice.tribes.dto.TribeOverviewResponse
import com.scrumdapp.tribeservice.tribes.dto.TribePatchDto
import com.scrumdapp.tribeservice.tribes.dto.TribeResponseDto
import com.scrumdapp.tribeservice.tribes.dto.TribeResponseGroup
import com.scrumdapp.tribeservice.tribes.dto.TribeGroupUser
import com.scrumdapp.tribeservice.tribes.dto.TribeGroupUserResponse
import com.scrumdapp.tribeservice.tribes.models.Tribe
import com.scrumdapp.tribeservice.tribes.models.TribeCoach
import com.scrumdapp.tribeservice.tribes.models.TribeGroup
import org.springframework.stereotype.Service

@Service
class TribeService(
    private val tribeRepository: TribeRepository,
    private val groupServiceRequests: GroupServiceRequests,
    private val tribeGroupRepository: TribeGroupRepository,
    private val tribeCoachRepository: TribeCoachRepository
) {

    fun getTribes(userId: Long): List<TribeOverviewResponse> {
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

    fun addGroupsToTribe(tribeId: Long, groupIds: List<Long>) {
        if (groupIds.isEmpty()) return

        val userGroups = PassportContent.fromStaticSecurityContext().userGroups
        val tribe = tribeRepository.getTribeById(tribeId) ?: throw RuntimeException("Not found")
        val tribeGroups = tribeGroupRepository.findByTribeId(tribeId)

        val newTribeGroups = mutableListOf<TribeGroup>()
        for (id in groupIds) {

            if (!userGroups.contains(id)) {
                throw RuntimeException("One or more groups could not be added")
            }

            if (!tribeGroups.any { it.groupId == id }) {
                newTribeGroups.add(TribeGroup().apply {
                    this.groupId = id
                    this.tribe = tribe
                })
            } else {
                throw RuntimeException("One or more groups could not be added")
            }
        }
        tribeGroupRepository.saveAll(newTribeGroups)
    }

    fun getTribeUsers(tribeId: Long): List<TribeGroupUserResponse> {
        val groupIds = tribeGroupRepository.findByTribeId(tribeId).map { it.groupId }

        val tribeGroupUsers = mutableListOf<TribeGroupUserResponse>()

        // if there are no more than a few 100 hundred groups in a tribe it will be fine
        for (id in groupIds) {
            val groupUsers = groupServiceRequests.getGroupUsers(id)
                .map { TribeGroupUser(it.userId, it.firstName, it.lastName) }

            tribeGroupUsers.add(TribeGroupUserResponse(id, groupUsers))
        }

        return tribeGroupUsers
    }

    private fun getTribeGroups(tribeId: Long): List<TribeResponseGroup> {
        val tribeGroups = tribeGroupRepository.findByTribeId(tribeId)
        val userGroups = groupServiceRequests.getGroups()

        val groups = mutableListOf<TribeResponseGroup>()
        for (tribeGroup in tribeGroups) {
            val group = userGroups.find { it.id == tribeGroup.groupId } ?: continue
            val item = TribeResponseGroup(
                tribeGroup.groupId,
                group.name,
                group.memberCount
            )
            groups.add(item)
        }
        return groups
    }
}

