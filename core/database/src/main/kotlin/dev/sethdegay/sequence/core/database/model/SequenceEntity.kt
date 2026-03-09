package dev.sethdegay.sequence.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Step
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "sequence",
    foreignKeys = [
        ForeignKey(
            entity = WorkspaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["workspace_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["workspace_id"])],
)
data class SequenceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid,

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

    @ColumnInfo(name = "workspace_id")
    val workspaceId: Uuid,
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
