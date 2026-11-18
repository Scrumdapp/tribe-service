package com.scrumdapp.tribeservice.checkpoints.dto

import com.scrumdapp.tribeservice.checkpoints.CheckpointPresence
import com.scrumdapp.tribeservice.checkpoints.SessionStatus
import java.time.LocalTime
import java.time.LocalDate

data class TribeCheckpointsDto(
    val sessionName: String,
    val groupSessions: List<TribeCheckpointGroup>,
)

data class TribeCheckpointGroup(
    val groupId: Long,
    val status: SessionStatus,
    val session: TribeCheckpointSession?,
)

data class TribeCheckpointSession(
    val sessionId: Long,
    val startedTime: LocalTime,
    val presences: List<TribeCheckpointPresence>,
    val averageStats: Double
)

data class TribeCheckpointPresence(
    val presence: CheckpointPresence,
    val name: String,
)

data class TribeRecentCheckpointDates(
    val fromDate: LocalDate?,
    val toDate: LocalDate?,
    val dates: List<LocalDate>
)