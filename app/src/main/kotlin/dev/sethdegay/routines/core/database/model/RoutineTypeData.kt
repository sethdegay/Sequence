package dev.sethdegay.routines.core.database.model

import kotlinx.serialization.Serializable

sealed interface RoutineTypeData {

    @Serializable
    data class WorkoutData(
        val warmUpDuration: Long? = null,
        val restDuration: Long? = null,
        val coolDownDuration: Long? = null,
    ) : RoutineTypeData
}
