package com.scrumdapp.tribeservice.groups

interface GroupServiceRequests {

    fun getGroups(): List<PartialGroup>

    fun getGroupUsers(groupId: Long): List<GroupUser>
}