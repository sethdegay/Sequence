package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.WorkspaceEntity
import dev.sethdegay.sequence.core.model.Workspace

fun Workspace.asEntity(): WorkspaceEntity = WorkspaceEntity(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
)