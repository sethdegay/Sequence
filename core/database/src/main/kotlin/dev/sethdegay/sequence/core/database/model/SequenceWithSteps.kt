package dev.sethdegay.sequence.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.sequence.core.model.Sequence

data class SequenceWithSteps(
    @Embedded
    val sequenceEntity: SequenceEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "sequence_id",
    )
    val stepEntities: List<StepEntity>,
)

fun SequenceWithSteps.asExternalModel(): Sequence =
    sequenceEntity.asExternalModel(stepEntities.map { it.asExternalModel() })