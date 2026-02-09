package dev.sethdegay.sequence.core.model

import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Sequence(
    val id: String = Uuid.random().toHexDashString(),
    val title: String,
    val description: String,
    val dateCreated: Instant,
    val dateModified: Instant,
    val steps: List<Step>,
    val totalDuration: Duration,
)