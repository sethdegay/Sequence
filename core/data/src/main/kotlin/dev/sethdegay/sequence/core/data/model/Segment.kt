package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.SegmentEntity
import dev.sethdegay.sequence.core.model.Segment
import kotlin.uuid.Uuid

fun Segment.asEntity(sequenceId: Uuid): SegmentEntity = SegmentEntity(
    id = id,
    title = title,
    duration = duration,
    order = order,
    sequenceId = sequenceId,
)