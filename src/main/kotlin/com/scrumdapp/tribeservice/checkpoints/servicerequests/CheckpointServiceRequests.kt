package com.scrumdapp.tribeservice.checkpoints.servicerequests

import java.time.LocalDate
import java.time.YearMonth
import com.scrumdapp.tribeservice.checkpoints.SessionDateDto

interface CheckpointServiceRequests {

    fun getCheckpointSessions(groupId: Long, date: LocalDate): List<CheckpointSession>

    fun getCheckpointsBySessionId(groupId: Long, sessionId: Long): List<Checkpoint>

    fun getCheckpointsBySessionName(groupId: Long, sessionName: String, date: LocalDate): List<Checkpoint>

    fun getCheckpointDates(
        groupId: Long,
        month: YearMonth?
    ): SessionDateDto

    fun getCheckpointMonths(groupId: Long): List<String>
}

