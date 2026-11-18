package com.scrumdapp.tribeservice.checkpoints.servicerequests

import com.scrumdapp.tribeservice.utils.DownstreamRequestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.YearMonth
import com.scrumdapp.tribeservice.checkpoints.SessionDateDto
@Component
class CheckpointServiceRequestImpl(
    @Value($$"${services.checkpointService}") private val checkpointServiceUrl: String,
    private val requestClient: DownstreamRequestClient
): CheckpointServiceRequests {

    override fun getCheckpointSessions(groupId: Long, date: LocalDate): List<CheckpointSession> {
        val uri = "$checkpointServiceUrl/groups/$groupId/sessions?date=$date"
        return requestClient.performRequest(HttpMethod.GET, uri)
    }

    override fun getCheckpointsBySessionId(groupId: Long, sessionId: Long): List<Checkpoint> {
        val uri = "$checkpointServiceUrl/groups/$groupId/checkpoints?session=$sessionId"
        return requestClient.performRequest(HttpMethod.GET, uri)
    }

    override fun getCheckpointsBySessionName(
        groupId: Long,
        sessionName: String,
        date: LocalDate
    ): List<Checkpoint> {

        val uri = "$checkpointServiceUrl/groups/$groupId/checkpoints?sessionName=$sessionName&date=$date"
        return requestClient.performRequest(HttpMethod.GET, uri)
    }
    override fun getCheckpointDates(
        groupId: Long,
        month: YearMonth?
    ): SessionDateDto {
        val uri = "$checkpointServiceUrl/groups/$groupId/sessions/dates" +
                if (month != null) "?month=$month" else ""

        return requestClient.performRequest(HttpMethod.GET, uri)
    }

    override fun getCheckpointMonths(
        groupId: Long
    ): List<String> {
        val uri = "$checkpointServiceUrl/groups/$groupId/sessions/months"

        return requestClient.performRequest(HttpMethod.GET, uri)
    }


}