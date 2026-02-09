package dev.sethdegay.routines.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Step
import kotlin.time.Duration

@Entity(
    tableName = "step",
    foreignKeys = [
        ForeignKey(
            entity = SequenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["sequence_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class StepEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "duration")
    val duration: Duration,

    @ColumnInfo(name = "list_order")
    val order: Int = 0,

    @ColumnInfo(name = "sequence_id")
    val sequenceId: String,
)

fun StepEntity.asExternalModel(): Step = Step(
    id = id,
    title = title,
    duration = duration,
    order = order,
)