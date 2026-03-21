package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.data.model.asEntity
import dev.sethdegay.sequence.core.database.dao.SequenceDao
import dev.sethdegay.sequence.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.uuid.Uuid

class LocalSequenceRepository @Inject constructor(
    private val sequenceDao: SequenceDao,
) : SequenceRepository {
    override suspend fun getSequence(id: Uuid): Sequence =
        sequenceDao.getSequence(id).asExternalModel()

    override fun getSequences(): Flow<List<Sequence>> = sequenceDao.getSequences()
        .map { sequences -> sequences.map { it.asExternalModel() } }

    override suspend fun saveSequence(sequence: Sequence, workspaceId: Uuid) {
        sequenceDao.upsertSequenceWithSegments(
            sequenceEntity = sequence.asEntity(workspaceId),
            segmentEntities = sequence.segments.map { it.asEntity(sequenceId = sequence.id) },
        )
    }

    override suspend fun delete(sequence: Sequence, workspaceId: Uuid) {
        sequenceDao.delete(sequence.asEntity(workspaceId))
    }
}