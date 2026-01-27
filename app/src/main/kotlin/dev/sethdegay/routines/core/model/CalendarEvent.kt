package dev.sethdegay.routines.core.model

import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class CalendarEvent(
    val id: String = Uuid.random().toHexDashString(),
    val start: Instant,
    val end: Instant,
    val duration: Duration,
    val routine: Routine,
)