package com.scrumdapp.tribeservice.tribes.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class TribePatchDto(

    @field:Size(max = 64, message = "Tribe name must be at most 64 characters long")
    @field:Pattern(regexp = "^[a-zA-Z0-9-_#!?(),. ]*$")
    val name: String?,

    @JsonProperty("background_preference")
    val backgroundPreference: String?,

    @JsonProperty("is_active")
    val isActive: Boolean?,
)