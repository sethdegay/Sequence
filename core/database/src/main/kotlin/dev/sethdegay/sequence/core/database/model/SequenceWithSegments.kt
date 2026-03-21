package dev.sethdegay.sequence.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.sequence.core.model.Sequence

data class SequenceWithSegments(
    @Embedded
    val sequenceEntity: SequenceEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "sequence_id",
    )
    val segmentEntities: List<SegmentEntity>,
)

fun SequenceWithSegments.asExternalModel(): Sequence =
    sequenceEntity.asExternalModel(segmentEntities.map { it.asExternalModel() })