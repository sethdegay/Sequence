package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.data.model.asEntity
import dev.sethdegay.sequence.core.database.dao.SequenceDao
import dev.sethdegay.sequence.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSequenceRepository @Inject constructor(
    private val sequenceDao: SequenceDao,
) : SequenceRepository {
    override suspend fun getSequence(id: String): Sequence =
        sequenceDao.getSequence(id).asExternalModel()

    override fun getSequences(): Flow<List<Sequence>> = sequenceDao.getSequences()
        .map { routines -> routines.map { it.asExternalModel() } }

    override suspend fun saveSequence(sequence: Sequence) {
        sequenceDao.upsertSequenceWithSteps(
            sequenceEntity = sequence.asEntity(),
            stepEntities = sequence.steps.map { it.asEntity(sequenceId = sequence.id) },
        )
    }

    override suspend fun delete(sequence: Sequence) {
        sequenceDao.delete(sequence.asEntity())
    }
}