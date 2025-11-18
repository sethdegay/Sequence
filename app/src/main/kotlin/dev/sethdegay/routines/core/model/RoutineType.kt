package dev.sethdegay.routines.core.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface RoutineType {
    @Serializable
    data object GENERIC : RoutineType

    @Serializable
    data class WORKOUT(
        val warmUpDuration: Duration? = null,
        val restDuration: Duration? = null,
        val coolDownDuration: Duration? = null,
    ) : RoutineType
}