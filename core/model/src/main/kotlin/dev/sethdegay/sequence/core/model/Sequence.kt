package dev.sethdegay.sequence.core.model

import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Sequence(
    val id: Uuid = Uuid.random(),
    val title: String,
    val description: String,
    val dateCreated: Instant,
    val dateModified: Instant,
    val segments: List<Segment>,
    val totalDuration: Duration,
    val rounds: Int,
)