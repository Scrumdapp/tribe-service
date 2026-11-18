package com.scrumdapp.tribeservice.checkpoints

import com.scrumdapp.tribeservice.checkpoints.dto.TribeCheckpointsDto
import com.scrumdapp.tribeservice.checkpoints.dto.TribeRecentCheckpointDates
import com.scrumdapp.tribeservice.security.HasTribeAccess
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/tribes/{tribeId}/sessions")
class TribeCheckpointController(
    private val tribeCheckpointService: TribeCheckpointService
) {

    @GetMapping
    @HasTribeAccess
    fun getCheckpoints(
        @PathVariable tribeId: Long,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate
    ): List<TribeCheckpointsDto> {
        return tribeCheckpointService.getCheckpoints(tribeId, date)
    }

    @GetMapping("/dates", params = ["!month"])
    @HasTribeAccess
    fun getRecentCheckpointDates(
        @PathVariable tribeId: Long
    ): TribeRecentCheckpointDates {
        val result = tribeCheckpointService
            .getCheckpointDates(tribeId, null)

        val dates = result.dates.map { it.date }.take(5)

        return TribeRecentCheckpointDates(
            fromDate = dates.minOrNull(),
            toDate = dates.maxOrNull(),
            dates = dates
        )
    }

    @GetMapping("/dates", params = ["month"])
    @HasTribeAccess
    fun getCheckpointDates(
        @PathVariable tribeId: Long,
        @RequestParam month: YearMonth
    ): SessionDateDto {
        return tribeCheckpointService.getCheckpointDates(tribeId, month)
    }

    @GetMapping("/months")
    @HasTribeAccess
    fun getCheckpointMonths(
        @PathVariable tribeId: Long
    ): List<String> {
        return tribeCheckpointService.getCheckpointMonths(tribeId)
    }
}