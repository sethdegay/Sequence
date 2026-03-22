package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Segment
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface SegmentRepository {
    fun getSegments(sequenceId: Uuid): Flow<List<Segment>>
    fun getSegment(id: Uuid): Flow<Segment>
    suspend fun saveSegment(segment: Segment, sequenceId: Uuid)
    suspend fun deleteSegment(segment: Segment, sequenceId: Uuid)
}