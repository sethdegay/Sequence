package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.StepEntity
import dev.sethdegay.sequence.core.model.Step

fun Step.asEntity(sequenceId: String): StepEntity = StepEntity(
    id = id,
    title = title,
    duration = duration,
    order = order,
    sequenceId = sequenceId,
)