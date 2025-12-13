package dev.sethdegay.routines.core.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Routine(
    val id: String = Uuid.random().toHexDashString(),
    val title: String,
    val description: String,
    val dateCreated: Instant,
    val dateModified: Instant,
    val tasks: List<Task>,
)