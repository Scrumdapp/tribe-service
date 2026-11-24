package com.scrumdapp.tribeservice.checkpoints

import com.scrumdapp.tribeservice.checkpoints.servicerequests.*
import com.scrumdapp.tribeservice.groups.GroupServiceRequests
import com.scrumdapp.tribeservice.tribes.TribeService
import com.scrumdapp.tribeservice.tribes.dto.TribeGroupResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.YearMonth

class TribeCheckpointServiceTest {

    private val tribeService =
        mock(TribeService::class.java)

    private val checkpointServiceRequests =
        mock(CheckpointServiceRequests::class.java)

    private val groupServiceRequests =
        mock(GroupServiceRequests::class.java)

    private val service = TribeCheckpointService(
        tribeService,
        checkpointServiceRequests,
        groupServiceRequests
    )

    private val tribeId = 1L
    private val groupId = 10L
    private val group = TribeGroupResponse(groupId, "Group 1", 5)

    @Test
    fun `returns empty list when tribe has no groups`() {
        val date = LocalDate.now()

        `when`(tribeService.getTribeGroups(tribeId))
            .thenReturn(emptyList())

        val result = service.getCheckpoints(tribeId, date)

        assertTrue(result.isEmpty())

        verifyNoInteractions(
            checkpointServiceRequests,
            groupServiceRequests
        )
    }

    @Test
    fun `returns closed checkpoint session`() {
        val date = LocalDate.now().minusDays(1)

        val session = CheckpointSession(
            id = 100L,
            startTime = date.atTime(9, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant(),
            duration = 30L,
            groupId = groupId,
            ownerId = 1L,
            name = "Daily"
        )

        `when`(tribeService.getTribeGroups(tribeId))
            .thenReturn(listOf(group))

        `when`(checkpointServiceRequests
            .getCheckpointSessions(groupId, date))
            .thenReturn(listOf(session))

        `when`(checkpointServiceRequests
            .getCheckpointsBySessionId(groupId, 100L))
            .thenReturn(emptyList())

        `when`(groupServiceRequests.getGroupUsers(groupId))
            .thenReturn(emptyList())

        val result = service.getCheckpoints(tribeId, date)

        assertEquals(1, result.size)
        assertEquals("Daily", result.first().sessionName)
        assertEquals(
            SessionStatus.CLOSED,
            result.first().groupSessions.first().status
        )
        assertEquals(
            LocalTime.of(9, 0),
            result.first().groupSessions.first().session!!.startedTime
        )
    }

    @Test
    fun `calculates average stars correctly`() {
        val date = LocalDate.now().minusDays(1)

        val session = CheckpointSession(
            id = 100L,
            startTime = date.atTime(9, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant(),
            duration = 30L,
            groupId = groupId,
            ownerId = 1L,
            name = "Daily"
        )

        val checkpoints = listOf(
            Checkpoint(1L, 100L, 1L, null, null, 4, null),
            Checkpoint(2L, 100L, 2L, null, null, 2, null)
        )

        `when`(tribeService.getTribeGroups(tribeId))
            .thenReturn(listOf(group))

        `when`(checkpointServiceRequests
            .getCheckpointSessions(groupId, date))
            .thenReturn(listOf(session))

        `when`(checkpointServiceRequests
            .getCheckpointsBySessionId(groupId, 100L))
            .thenReturn(checkpoints)

        `when`(groupServiceRequests.getGroupUsers(groupId))
            .thenReturn(emptyList())

        val result = service.getCheckpoints(tribeId, date)

        assertEquals(
            3.0,
            result.first().groupSessions.first()
                .session!!.averageStats
        )
    }

    @Test
    fun `returns checkpoint dates`() {
        val date = LocalDate.of(2026, 11, 18)
        val month = YearMonth.of(2026, 11)

        val response = SessionDateDto(
            fromDate = date,
            toDate = date,
            dates = listOf(
                SessionDates(date, listOf(100L))
            )
        )

        `when`(tribeService.getTribeGroups(tribeId))
            .thenReturn(listOf(group))

        `when`(checkpointServiceRequests
            .getCheckpointDates(groupId, month))
            .thenReturn(response)

        val result = service.getCheckpointDates(tribeId, month)

        assertEquals(response, result)
    }

    @Test
    fun `returns unique checkpoint months`() {
        val secondGroup = TribeGroupResponse(20L, "Group 2", 4)

        `when`(tribeService.getTribeGroups(tribeId))
            .thenReturn(listOf(group, secondGroup))

        `when`(checkpointServiceRequests
            .getCheckpointMonths(groupId))
            .thenReturn(listOf("2026-11", "2026-10"))

        `when`(checkpointServiceRequests
            .getCheckpointMonths(20L))
            .thenReturn(listOf("2026-11", "2026-09"))

        val result = service.getCheckpointMonths(tribeId)

        assertEquals(
            listOf("2026-11", "2026-10", "2026-09"),
            result
        )
    }
}