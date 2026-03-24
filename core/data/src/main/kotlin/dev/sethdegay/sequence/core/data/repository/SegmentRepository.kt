package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Segment
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface SegmentRepository {
    fun getSegments(sequenceId: Uuid): Flow<List<Segment>>
    suspend fun getSegment(id: Uuid): Segment
    suspend fun saveSegment(segment: Segment, sequenceId: Uuid)
    suspend fun saveSegments(segments: List<Segment>, sequenceId: Uuid)
    suspend fun deleteSegment(segment: Segment, sequenceId: Uuid)
}