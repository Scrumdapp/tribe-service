package com.scrumdapp.tribeservice.checkpoints

import com.scrumdapp.tribeservice.checkpoints.dto.*
import com.scrumdapp.tribeservice.checkpoints.servicerequests.CheckpointServiceRequests
import com.scrumdapp.tribeservice.groups.GroupServiceRequests
import com.scrumdapp.tribeservice.tribes.TribeService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@Service
class TribeCheckpointService(
    private val tribeService: TribeService,
    private val checkpointServiceRequests: CheckpointServiceRequests,
    private val groupServiceRequests: GroupServiceRequests
) {

    fun getCheckpoints(
        tribeId: Long,
        date: LocalDate
    ): List<TribeCheckpointsDto> {

        val groups = tribeService.getTribeGroups(tribeId)

        val sessionsByGroup = groups.associate { group ->
            group.id to checkpointServiceRequests
                .getCheckpointSessions(group.id, date)
        }

        val sessionNames = sessionsByGroup.values
            .flatten()
            .mapNotNull { it.name }
            .distinct()

        val usersByGroup = mutableMapOf<Long, List<com.scrumdapp.tribeservice.groups.GroupUser>>()

        return sessionNames.map { sessionName ->

            val groupSessions = groups.map { group ->

                val session = sessionsByGroup[group.id]
                    ?.find { it.name == sessionName }

                if (session == null) {
                    TribeCheckpointGroup(
                        groupId = group.id,
                        status = SessionStatus.NOT_STARTED,
                        session = null
                    )
                } else {

                    val checkpoints = checkpointServiceRequests
                        .getCheckpointsBySessionId(
                            group.id,
                            session.id
                        )

                    val users = usersByGroup.getOrPut(group.id) {
                        groupServiceRequests.getGroupUsers(group.id)
                    }

                    val presences = checkpoints.mapNotNull { checkpoint ->
                        val presence = checkpoint.presence
                            ?: return@mapNotNull null

                        val user = users.find {
                            it.userId == checkpoint.groupUser
                        } ?: return@mapNotNull null

                        TribeCheckpointPresence(
                            presence = presence,
                            name = "${user.firstName} ${user.lastName}"
                        )
                    }

                    val stars = checkpoints.mapNotNull { it.stars }

                    val averageStars = if (stars.isEmpty()) {
                        0.0
                    } else {
                        stars.average()
                    }

                    val startTime = LocalTime.parse(session.startTime)

                    val endTime = date.atTime(startTime)
                        .plusMinutes(session.duration)

                    val status = if (
                        java.time.LocalDateTime.now().isBefore(endTime)
                    ) {
                        SessionStatus.OPEN
                    } else {
                        SessionStatus.CLOSED
                    }

                    TribeCheckpointGroup(
                        groupId = group.id,
                        status = status,
                        session = TribeCheckpointSession(
                            sessionId = session.id,
                            startedTime = startTime,
                            presences = presences,
                            averageStats = averageStars
                        )
                    )
                }
            }

            TribeCheckpointsDto(
                sessionName = sessionName,
                groupSessions = groupSessions
            )
        }
    }
    fun getCheckpointDates(
        tribeId: Long,
        month: YearMonth?
    ): SessionDateDto {

        val groups = tribeService.getTribeGroups(tribeId)

        val responses = groups.map { group ->
            checkpointServiceRequests.getCheckpointDates(
                group.id,
                month
            )
        }

        val dates = responses
            .flatMap { it.dates }
            .groupBy { it.date }
            .map { (date, sessions) ->
                SessionDates(
                    date = date,
                    sessions = sessions
                        .flatMap { it.sessions }
                        .distinct()
                )
            }
            .sortedByDescending { it.date }

        return SessionDateDto(
            fromDate = responses.mapNotNull { it.fromDate }.minOrNull(),
            toDate = responses.mapNotNull { it.toDate }.maxOrNull(),
            dates = dates
        )
    }

    fun getCheckpointMonths(
        tribeId: Long
    ): List<String> {

        return tribeService.getTribeGroups(tribeId)
            .flatMap { group ->
                checkpointServiceRequests.getCheckpointMonths(group.id)
            }
            .distinct()
            .sortedDescending()
    }
}