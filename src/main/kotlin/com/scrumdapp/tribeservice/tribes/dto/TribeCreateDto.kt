package com.scrumdapp.tribeservice.tribes.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class TribeCreateDto (

    @field:Size(max = 64, message = "Tribe name must be at most 64 characters long")
    @field:Pattern(regexp = "^[a-zA-Z0-9-_#!?(),. \\-]*$")
    val name: String,
)