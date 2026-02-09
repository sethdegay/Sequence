package dev.sethdegay.routines.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Step
import kotlin.time.Duration
import kotlin.time.Instant

@Entity(tableName = "sequence")
data class SequenceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "date_created")
    val dateCreated: Instant,

    @ColumnInfo(name = "date_modified")
    val dateModified: Instant,

    @ColumnInfo(name = "total_duration")
    val totalDuration: Duration,
)

fun SequenceEntity.asExternalModel(steps: List<Step>): Sequence = Sequence(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
    steps = steps,
    totalDuration = totalDuration,
)
