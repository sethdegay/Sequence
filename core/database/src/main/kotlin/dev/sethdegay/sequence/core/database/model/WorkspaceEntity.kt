package dev.sethdegay.sequence.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Workspace
import kotlin.time.Instant

@Entity(tableName = "workspace")
data class WorkspaceEntity(
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
)

fun WorkspaceEntity.asExternalModel(sequences: List<Sequence>): Workspace = Workspace(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
    sequences = sequences,
)