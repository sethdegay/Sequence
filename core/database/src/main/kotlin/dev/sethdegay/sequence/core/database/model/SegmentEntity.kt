package dev.sethdegay.sequence.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.Segment
import kotlin.time.Duration
import kotlin.uuid.Uuid

@Entity(
    tableName = "segment",
    foreignKeys = [
        ForeignKey(
            entity = SequenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["sequence_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["sequence_id"])],
)
data class SegmentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "duration")
    val duration: Duration,

    @ColumnInfo(name = "list_order")
    val order: Int = 0,

    @ColumnInfo(name = "sequence_id")
    val sequenceId: Uuid,
)

fun SegmentEntity.asExternalModel(): Segment = Segment(
    id = id,
    title = title,
    duration = duration,
    order = order,
)