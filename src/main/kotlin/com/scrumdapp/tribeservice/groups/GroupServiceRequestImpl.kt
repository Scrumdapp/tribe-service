package com.scrumdapp.tribeservice.groups

import com.scrumdapp.tribeservice.utils.DownstreamRequestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class GroupServiceRequestImpl(
    @Value($$"${services.groupService}") private val groupServiceUrl: String,
    private val requestClient: DownstreamRequestClient
): GroupServiceRequests {
    override fun getGroups(): List<PartialGroup> {
        return requestClient.performRequest(HttpMethod.GET, "$groupServiceUrl/groups")
    }

    override fun getGroupUsers(groupId: Long): List<GroupUser> {
        return requestClient.performRequest(HttpMethod.GET, "$groupServiceUrl/groups/$groupId/users")
    }
}