package com.scrumdapp.tribeservice.tribes.dto

data class TribeOverviewResponseDto(
    val id: Long,
    val name: String,
    val backgroundPreference: String?,
    val groupCount: Int,
    val isActive: Boolean,
)