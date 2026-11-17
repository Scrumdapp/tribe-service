package com.scrumdapp.tribeservice.checkpoints.servicerequests

import com.scrumdapp.tribeservice.checkpoints.CheckpointPresence
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

@Service
@Primary
@Profile("use-mock-service")
class CheckpointServiceRequestMock(
    private val logger: Logger = LoggerFactory.getLogger(CheckpointServiceRequestMock::class.java)
): CheckpointServiceRequests {
    init {
        logger.info("Using mock service to perform requests to checkpoint service.")
    }

    // Truly hideous mocking data, use exclusively for debugging
    override fun getCheckpointSessions(groupId: Long, date: LocalDate): List<CheckpointSession> {
        return listOf(
            CheckpointSession(1, randomWorktimeOnDate(date).toString(), 15, groupId, 1, "Check-in"),
            CheckpointSession(2, randomWorktimeOnDate(date).toString(), 15, groupId, 1, "Check-up"),
            CheckpointSession(4, randomWorktimeOnDate(date).toString(), 15, groupId, 1, "Check-out"),

        )
    }

    override fun getCheckpointsBySessionId(groupId: Long, sessionId: Long): List<Checkpoint> {
        return getCheckpoints(sessionId)
    }

    override fun getCheckpointsBySessionName(groupId: Long, sessionName: String, date: LocalDate): List<Checkpoint> {
        return getCheckpoints(42)
    }

    private fun getCheckpoints(sessionId: Long): List<Checkpoint> {
        return listOf(
            Checkpoint(1,sessionId, 1, CheckpointPresence.ON_TIME, null, null, null),
            Checkpoint(4,sessionId, 85, CheckpointPresence.VERIFIED_LATE, null, null, null),
            Checkpoint(6,sessionId, 46, CheckpointPresence.ON_TIME, null, null, null)
        )
    }

    private fun randomWorktimeOnDate(date: LocalDate): LocalDateTime {
        val timedDate = date.atStartOfDay()
        timedDate.plusHours(9 + Random.nextLong(0, 9))
        timedDate.plusMinutes(Random.nextLong(0, 60))
        timedDate.plusSeconds(Random.nextLong(0, 60))
        return timedDate
    }
}