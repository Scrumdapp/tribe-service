package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.tribeservice.tribes.dto.TribeGroupResponse
import com.scrumdapp.tribeservice.tribes.dto.TribeOverviewResponseDto
import com.scrumdapp.tribeservice.tribes.dto.TribePatchDto
import com.scrumdapp.tribeservice.tribes.dto.TribeResponseDto
import com.scrumdapp.tribeservice.tribes.models.Tribe

fun Tribe.toOverviewResponse(groupCount: Int): TribeOverviewResponseDto {
    return TribeOverviewResponseDto(id, name, backgroundPreference,groupCount, isActive)
}

fun Tribe.toResponse(groups: List<TribeGroupResponse>): TribeResponseDto {
    return TribeResponseDto(
        this.id,
        this.name,
        this.backgroundPreference,
        groups.size,
        groups,
        this.ownerId,
        this.isActive
    )
}

fun Tribe.patch(dto: TribePatchDto): Tribe {
    return this.apply {
        dto.name?.let { name = it }
        dto.backgroundPreference?.let { backgroundPreference = it }
        dto.isActive?.let { isActive = it }
    }
}