package com.scrumdapp.tribeservice.tribes.dto

data class TribeResponseDto(
    val id: Long,
    val name: String,
    val backgroundPreference: String?,
    val groupCount: Int,
    val groups: List<TribeResponseGroup>,
    val ownerId: Long,
    val isActive: Boolean,
)

data class TribeResponseGroup(
    val id: Long,
    val name: String,
    val userCount: Int,
)