package com.scrumdapp.tribeservice.tribes.dto

data class TribeGroupUserResponse(
    val groupId: Long,
    val users: List<TribeGroupUser>
)

data class TribeGroupUser(
    val userId: Long,
    val firstName: String,
    val lastName: String,
)