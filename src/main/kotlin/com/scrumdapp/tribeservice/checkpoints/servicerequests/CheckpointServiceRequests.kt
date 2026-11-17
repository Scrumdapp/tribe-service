package com.scrumdapp.tribeservice.checkpoints.servicerequests

import java.time.LocalDate

interface CheckpointServiceRequests {

    fun getCheckpointSessions(groupId: Long, date: LocalDate): List<CheckpointSession>

    fun getCheckpointsBySessionId(groupId: Long, sessionId: Long): List<Checkpoint>

    fun getCheckpointsBySessionName(groupId: Long, sessionName: String, date: LocalDate): List<Checkpoint>
}

