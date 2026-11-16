package com.scrumdapp.tribeservice.tribes.models

import java.io.Serializable

data class TribeCoachId(
    val userId: Long,
    val tribe: Tribe
): Serializable