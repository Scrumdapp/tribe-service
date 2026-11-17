package com.scrumdapp.tribeservice.checkpoints.servicerequests

import com.scrumdapp.tribeservice.checkpoints.CheckpointPresence

data class Checkpoint(
    val id: Long,
    val sessionId: Long,
    val groupUser: Long,
    val presence: CheckpointPresence?,
    val impediment: String?,
    val stars: Int?,
    val comment: String?,
)