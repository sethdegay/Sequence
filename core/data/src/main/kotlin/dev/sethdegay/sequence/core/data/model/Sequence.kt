package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.SequenceEntity
import dev.sethdegay.sequence.core.model.Sequence

fun Sequence.asEntity(): SequenceEntity = SequenceEntity(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
    totalDuration = totalDuration,
)