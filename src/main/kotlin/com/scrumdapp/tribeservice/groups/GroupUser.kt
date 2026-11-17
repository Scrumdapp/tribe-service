package com.scrumdapp.tribeservice.groups

data class GroupUser (
    val userId: Long,
    val groupId: Long,
    val firstName: String,
    val lastName: String,
    val isGhost: Boolean
)