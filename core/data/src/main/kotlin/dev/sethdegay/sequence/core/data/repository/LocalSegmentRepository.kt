package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.data.model.asEntity
import dev.sethdegay.sequence.core.database.dao.SegmentDao
import dev.sethdegay.sequence.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.Segment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.uuid.Uuid

class LocalSegmentRepository @Inject constructor(
    private val segmentDao: SegmentDao,
) : SegmentRepository {
    override fun getSegments(sequenceId: Uuid): Flow<List<Segment>> =
        segmentDao.getSegments(sequenceId).map { entities -> entities.map { it.asExternalModel() } }

    override suspend fun getSegment(id: Uuid): Segment =
        segmentDao.getSegment(id).asExternalModel()

    override suspend fun saveSegment(segment: Segment, sequenceId: Uuid) {
        segmentDao.upsertSegment(segment.asEntity(sequenceId))
    }

    override suspend fun deleteSegment(segment: Segment, sequenceId: Uuid) {
        segmentDao.deleteSegment(segment.asEntity(sequenceId))
    }
}