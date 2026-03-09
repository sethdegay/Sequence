package dev.sethdegay.sequence.core.model

import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class CalendarEvent(
    val id: Uuid = Uuid.random(),
    val start: Instant,
    val end: Instant,
    val duration: Duration,
    val sequence: Sequence,
)