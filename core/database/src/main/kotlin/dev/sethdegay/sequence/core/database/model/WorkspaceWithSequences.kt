package dev.sethdegay.sequence.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.sequence.core.model.Workspace

data class WorkspaceWithSequences(
    @Embedded
    val workspaceEntity: WorkspaceEntity,

    @Relation(
        entity = SequenceEntity::class,
        parentColumn = "id",
        entityColumn = "workspace_id",
    )
    val sequenceEntities: List<SequenceWithSteps>,
)

fun WorkspaceWithSequences.asExternalModel(): Workspace =
    workspaceEntity.asExternalModel(sequenceEntities.map { it.asExternalModel() })