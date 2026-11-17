package com.scrumdapp.tribeservice.checkpoints

import com.scrumdapp.tribeservice.checkpoints.dto.TribeCheckpointsDto
import com.scrumdapp.tribeservice.security.HasTribeAccess
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/tribes/{tribeId}/sessions")
class TribeCheckpointController(
    private val tribeCheckpointService: TribeCheckpointService) {

    @GetMapping
    @HasTribeAccess
    fun getCheckpoints(
        @PathVariable tribeId: Long,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate
    ): List<TribeCheckpointsDto> {
        TODO("Implement checkpoint retrieval")
    }

    @GetMapping("/dates")
    @HasTribeAccess
    fun getCheckpointDates(
        @PathVariable tribeId: Long,
        @RequestParam(required = false) month: YearMonth?
    ): SessionDateDto {
        TODO("Implement checkpoint date retrieval")
    }

    @GetMapping("/months")
    @HasTribeAccess
    fun getCheckpointMonths(
        @PathVariable tribeId: Long
    ): List<String> {
        TODO("Implement checkpoint month retrieval")
    }
}