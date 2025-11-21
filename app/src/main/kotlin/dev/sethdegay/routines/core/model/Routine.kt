package dev.sethdegay.routines.core.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Routine(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val routineType: RoutineType,
    val dateCreated: Instant,
    val dateModified: Instant,
    val tasks: List<Task>,
)