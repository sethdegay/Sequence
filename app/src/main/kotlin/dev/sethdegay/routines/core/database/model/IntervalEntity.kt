package dev.sethdegay.routines.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.sethdegay.routines.core.model.Interval
import kotlin.time.Duration

@Entity(
    tableName = "interval",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routine_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class IntervalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "duration")
    val duration: Duration,

    @ColumnInfo(name = "list_order")
    val order: Int = 0,

    @ColumnInfo(name = "routine_id")
    val routineId: Long? = null,
)

fun IntervalEntity.asExternalModel(): Interval = Interval(
    id = id,
    title = title,
    duration = duration,
    order = order,
)