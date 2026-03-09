package dev.sethdegay.sequence.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.sethdegay.sequence.core.database.model.SequenceEntity
import dev.sethdegay.sequence.core.database.model.SequenceWithSteps
import dev.sethdegay.sequence.core.database.model.StepEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

@Suppress("FunctionName")
@Dao
interface SequenceDao {
    @Query("SELECT * FROM sequence WHERE id = :id")
    suspend fun _getSequence(id: Uuid): SequenceEntity

    suspend fun getSequence(id: Uuid): SequenceWithSteps = SequenceWithSteps(
        sequenceEntity = _getSequence(id),
        stepEntities = _getSequenceSteps(id),
    )

    @Query("SELECT * FROM sequence")
    fun _getSequences(): Flow<List<SequenceEntity>>

    @Query("SELECT * FROM step WHERE sequence_id = :id ORDER BY list_order ASC")
    suspend fun _getSequenceSteps(id: Uuid): List<StepEntity>

    fun getSequences(): Flow<List<SequenceWithSteps>> = _getSequences().map { entities ->
        entities.map { entity ->
            SequenceWithSteps(
                sequenceEntity = entity,
                stepEntities = _getSequenceSteps(entity.id),
            )
        }
    }

    @Upsert
    suspend fun _upsertSequence(sequenceEntity: SequenceEntity)

    @Upsert
    suspend fun _upsertSteps(taskEntities: List<StepEntity>)

    @Transaction
    suspend fun upsertSequenceWithSteps(
        sequenceEntity: SequenceEntity,
        stepEntities: List<StepEntity>? = null,
    ) {
        _upsertSequence(sequenceEntity)
        if (stepEntities != null) {
            _upsertSteps(stepEntities)
        }
    }

    @Delete
    suspend fun delete(sequenceEntity: SequenceEntity)
}