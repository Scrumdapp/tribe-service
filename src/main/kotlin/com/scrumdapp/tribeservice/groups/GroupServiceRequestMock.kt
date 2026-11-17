package com.scrumdapp.tribeservice.groups

import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Primary
@Profile("use-mock-service")
class GroupServiceRequestMock(): GroupServiceRequests {
    override fun getGroups(): List<PartialGroup> {
        return listOf(
            PartialGroup(1, "Inner space", null, 4),
            PartialGroup(2, "Middle space", "4_5", 6),
            PartialGroup(6, "Outer space", "bg_green", 2),
        )
    }

    override fun getGroupUsers(groupId: Long): List<GroupUser> {
        return listOf(
            GroupUser(1, groupId, "Johny", "Dab", false),
            GroupUser(2, groupId, "KVM", "", false),
            GroupUser(4, groupId, "Meh", "Mc Meh", false),
            GroupUser(6, groupId, "Max", "", false),
            GroupUser(22, groupId, "John", "Doe", false),
        )
    }
}