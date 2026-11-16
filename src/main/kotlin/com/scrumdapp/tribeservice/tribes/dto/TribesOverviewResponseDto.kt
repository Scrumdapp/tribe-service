package com.scrumdapp.tribeservice.tribes.dtos

data class TribesOverviewResponseDto(
    val id: Long,
    val name: String,
    val backgroundPreference: String,
    val groupCount: Int,
    val isActive: Boolean,
)