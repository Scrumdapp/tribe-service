package com.scrumdapp.tribeservice.tribes.dtos

data class TribeResponseDto(
    val id: Long,
    val name: String,
    val backgroundPreference: String,
    val groupCount: Int,
    val groups: List<TribeGroups>,
    val ownerId: Long,
    val isActive: Boolean,
)

data class TribeGroups(
    val id: Long,
    val name: String,
    val memberCount: Int,
)