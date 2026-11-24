package com.scrumdapp.tribeservice.checkpoints.servicerequests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper
import java.time.Instant

class CheckpointSessionDeserializationTest {

    @Test
    fun `deserializes checkpoint service ISO instant start time`() {
        val json = """
            [
              {
                "id": 100,
                "startTime": "2026-11-24T11:00:00Z",
                "duration": 30,
                "groupId": 10,
                "ownerId": 1,
                "name": "Daily"
              }
            ]
        """.trimIndent()

        // Match the downstream request client's directly constructed ObjectMapper.
        val objectMapper = ObjectMapper()
        val sessions = objectMapper.readValue(
            json,
            object : TypeReference<List<CheckpointSession>>() {}
        )

        assertEquals(1, sessions.size)
        assertEquals(
            Instant.parse("2026-11-24T11:00:00Z"),
            sessions.single().startTime
        )
    }
}
