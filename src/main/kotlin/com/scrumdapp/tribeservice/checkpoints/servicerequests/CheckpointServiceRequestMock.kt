package com.scrumdapp.tribeservice.checkpoints.servicerequests

import com.scrumdapp.tribeservice.checkpoints.CheckpointPresence
import com.scrumdapp.tribeservice.checkpoints.SessionDateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.YearMonth
import kotlin.random.Random
import com.scrumdapp.tribeservice.checkpoints.SessionDates

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
            CheckpointSession(1, randomWorktimeOnDate(date), 15, groupId, 1, "Check-in"),
            CheckpointSession(2, randomWorktimeOnDate(date), 15, groupId, 1, "Check-up"),
            CheckpointSession(4, randomWorktimeOnDate(date), 15, groupId, 1, "Check-out"),

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
            Checkpoint(1, sessionId, 1, CheckpointPresence.ON_TIME, null, null, null),
            Checkpoint(4, sessionId, 85, CheckpointPresence.VERIFIED_LATE, null, null, null),
            Checkpoint(6, sessionId, 46, CheckpointPresence.ON_TIME, null, null, null)
        )
    }

    private fun randomWorktimeOnDate(date: LocalDate): Instant {
        return date.atStartOfDay()
            .plusHours(9 + Random.nextLong(0, 9))
            .plusMinutes(Random.nextLong(0, 60))
            .plusSeconds(Random.nextLong(0, 60))
            .atZone(ZoneId.systemDefault())
            .toInstant()
    }

    override fun getCheckpointDates(
        groupId: Long,
        month: YearMonth?
    ): SessionDateDto {
        val dates = if (month == null) {
            (0L..4L).map { LocalDate.now().minusDays(it) }
        } else {
            (1..month.lengthOfMonth()).map { month.atDay(it) }
        }

        return SessionDateDto(
            fromDate = dates.minOrNull(),
            toDate = dates.maxOrNull(),
            dates = dates.map { date ->
                SessionDates(
                    date = date,
                    sessions = getCheckpointSessions(groupId, date)
                        .map { it.id }
                )
            }
        )
    }

    override fun getCheckpointMonths(
        groupId: Long
    ): List<String> {
        return (0L..2L).map {
            YearMonth.now().minusMonths(it).toString()
        }
    }
}