package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.passportplugin.annotations.passportController.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.tribeservice.security.HasTribeAccess
import com.scrumdapp.tribeservice.tribes.dto.TribeCreateDto
import com.scrumdapp.tribeservice.tribes.dto.TribeGroupImportDto
import com.scrumdapp.tribeservice.tribes.dto.TribeGroupUserResponse
import com.scrumdapp.tribeservice.tribes.dto.TribeOverviewResponse
import com.scrumdapp.tribeservice.tribes.dto.TribePatchDto
import com.scrumdapp.tribeservice.tribes.dto.TribeResponseDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tribes")
class TribeController(
    private val tribeService: TribeService
) {

    @GetMapping
    fun getTribes(
        @Passport passport: PassportContent
    ): List<TribeOverviewResponse> {
        return tribeService.getTribes(passport.userId)
    }

    @PostMapping
    fun createTribe(
        @Passport passport: PassportContent,
        @Valid @RequestBody dto: TribeCreateDto
    ): TribeResponseDto {
        return tribeService.createTribe(dto, passport.userId)
    }

    @GetMapping("/{tribeId}")
    @HasTribeAccess
    fun getTribe(
        @PathVariable tribeId: Long,
    ): TribeResponseDto {
        return tribeService.getTribeById(tribeId)
    }

    @PatchMapping("/{tribeId}")
    @HasTribeAccess
    fun updateTribe(
        @PathVariable tribeId: Long,
        @Valid @RequestBody dto: TribePatchDto
    ): TribeResponseDto {
        return tribeService.updateTribe(tribeId, dto)
    }

    @PostMapping("/{tribeId}/groups/import") // Not final endpoint yet
    @HasTribeAccess
    fun importGroups(
        @PathVariable tribeId: Long,
        @RequestBody dto: TribeGroupImportDto
    ) {
        tribeService.addGroupsToTribe(tribeId, dto.groupIds)
    }

    @GetMapping("/{tribeId}/users")
    @HasTribeAccess
    fun getTribeUsers(
        @PathVariable tribeId: Long,
    ): List<TribeGroupUserResponse> {
        return tribeService.getTribeUsers(tribeId)
    }
}

