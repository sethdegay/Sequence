package dev.sethdegay.routines.core.model

import kotlin.time.Duration
import kotlin.uuid.Uuid

data class Task(
    val id: String = Uuid.random().toHexDashString(),
    val title: String,
    val duration: Duration,
    val order: Int = 0,
)