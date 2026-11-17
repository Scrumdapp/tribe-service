package com.scrumdapp.tribeservice.tribes.dto

data class TribeResponseDto(
    val id: Long,
    val name: String,
    val backgroundPreference: String?,
    val groupCount: Int,
    val groups: List<TribeGroupResponse>,
    val ownerId: Long,
    val isActive: Boolean,
)

data class TribeGroupResponse(
    val id: Long,
    val name: String,
    val memberCount: Int,
)