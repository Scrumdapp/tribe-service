package com.scrumdapp.tribeservice.checkpoints.servicerequests

data class CheckpointSession(
    val id: Long,
    val startTime: String,
    val duration: Long,
    val groupId: Long,
    val ownerId: Long,
    val name: String?,
)