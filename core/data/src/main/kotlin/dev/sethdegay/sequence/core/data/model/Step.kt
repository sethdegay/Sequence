package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.StepEntity
import dev.sethdegay.sequence.core.model.Step
import kotlin.uuid.Uuid

fun Step.asEntity(sequenceId: Uuid): StepEntity = StepEntity(
    id = id,
    title = title,
    duration = duration,
    order = order,
    sequenceId = sequenceId,
)