package com.scrumdapp.tribeservice.checkpoints

import java.time.LocalDate

data class SessionDateDto(
    val fromDate: LocalDate?,
    val toDate: LocalDate?,
    val dates: List<SessionDates>,
)

data class SessionDates(
    val date: LocalDate,
    val sessions: List<Long>
)