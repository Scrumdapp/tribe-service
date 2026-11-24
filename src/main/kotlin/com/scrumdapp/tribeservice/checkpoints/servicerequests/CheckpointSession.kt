package com.scrumdapp.tribeservice.checkpoints.servicerequests

import java.time.Instant

data class CheckpointSession(
    val id: Long,
    val startTime: Instant,
    val duration: Long,
    val groupId: Long,
    val ownerId: Long,
    val name: String?,
)