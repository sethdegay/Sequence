package dev.sethdegay.sequence.core.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Sequence(
    val id: Uuid = Uuid.random(),
    val title: String,
    val description: String,
    val dateCreated: Instant,
    val dateModified: Instant,
    val segments: List<Segment>,
    val rounds: Int,
) {
    val repeatedDuration: Duration = segments.calculateTotalDuration(rounds)
    val totalDuration: Duration = segments.calculateTotalDuration()
}

fun List<Segment>.calculateTotalDuration(scale: Int = 1): Duration {
    return this.sumOf { it.duration.inWholeSeconds }.seconds.times(scale)
}