package dev.sethdegay.sequence.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Library
import dev.sethdegay.sequence.core.model.Sequence
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(tableName = "library")
data class LibraryEntity(
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
)

fun LibraryEntity.asExternalModel(sequences: List<Sequence>): Library = Library(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
    sequences = sequences,
)