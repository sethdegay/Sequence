package dev.sethdegay.routines.core.model

import kotlin.time.Duration

sealed interface RoutineType {
    data object GENERIC : RoutineType

    data class WORKOUT(
        val warmUpDuration: Duration? = null,
        val restDuration: Duration? = null,
        val coolDownDuration: Duration? = null,
    ) : RoutineType
}