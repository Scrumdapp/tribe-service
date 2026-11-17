package com.scrumdapp.tribeservice.groups

import com.fasterxml.jackson.annotation.JsonProperty

data class PartialGroup(
    val id: Long,
    val name: String,
    @JsonProperty("background_preference")
    val backgroundPreference: String?,
    @JsonProperty("background_preference")
    val memberCount: Int // This value hasn't been added to gr service yet
)