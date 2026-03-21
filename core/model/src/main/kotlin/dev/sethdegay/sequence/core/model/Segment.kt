package dev.sethdegay.sequence.core.model

import kotlin.time.Duration
import kotlin.uuid.Uuid

data class Segment(
    val id: Uuid = Uuid.random(),
    val title: String,
    val duration: Duration,
    val order: Int = 0,
)