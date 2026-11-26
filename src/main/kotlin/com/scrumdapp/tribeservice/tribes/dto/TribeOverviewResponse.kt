package com.scrumdapp.tribeservice.tribes.dto

data class TribeOverviewResponse(
    val id: Long,
    val name: String,
    val backgroundPreference: String?,
    val groupCount: Int,
    val isActive: Boolean,
)