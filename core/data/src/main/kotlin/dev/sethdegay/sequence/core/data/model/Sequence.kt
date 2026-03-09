package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.SequenceEntity
import dev.sethdegay.sequence.core.model.Sequence
import kotlin.uuid.Uuid

fun Sequence.asEntity(workspaceId: Uuid): SequenceEntity = SequenceEntity(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
    totalDuration = totalDuration,
    workspaceId = workspaceId,
)