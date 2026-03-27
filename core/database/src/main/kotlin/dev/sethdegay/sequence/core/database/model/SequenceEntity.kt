package dev.sethdegay.sequence.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.Sequence
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "sequence",
    foreignKeys = [
        ForeignKey(
            entity = LibraryEntity::class,
            parentColumns = ["id"],
            childColumns = ["library_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["library_id"])],
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

    @ColumnInfo(name = "library_id")
    val libraryId: Uuid,
)

fun SequenceEntity.asExternalModel(segments: List<Segment>): Sequence = Sequence(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
    segments = segments,
    totalDuration = totalDuration,
)
