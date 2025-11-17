package dev.sethdegay.routines.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.sethdegay.routines.core.database.util.DurationConverter
import dev.sethdegay.routines.core.model.Interval
import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.RoutineType
import kotlin.time.Instant

@Entity(tableName = "routine")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "routine_type_data")
    val routineTypeData: RoutineTypeData? = null,

    @ColumnInfo(name = "date_created")
    val dateCreated: Instant,

    @ColumnInfo(name = "date_modified")
    val dateModified: Instant,
)

fun RoutineEntity.asExternalModel(intervals: List<Interval>): Routine = Routine(
    id = id,
    title = title,
    description = description,
    routineType = routineTypeData.asExternalModel(),
    dateCreated = dateCreated,
    dateModified = dateModified,
    intervals = intervals,
)

private fun RoutineTypeData?.asExternalModel(): RoutineType {
    if (this == null) return RoutineType.GENERIC
    val durationConverter = DurationConverter()
    return when (this) {
        is RoutineTypeData.WorkoutData -> RoutineType.WORKOUT(
            warmUpDuration = durationConverter.longToDuration(warmUpDuration),
            restDuration = durationConverter.longToDuration(restDuration),
            coolDownDuration = durationConverter.longToDuration(coolDownDuration),
        )
    }
}
