package dev.sethdegay.sequence.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.sethdegay.sequence.core.database.model.SegmentEntity
import dev.sethdegay.sequence.core.database.model.SequenceEntity
import dev.sethdegay.sequence.core.database.model.SequenceWithSegments
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

@Suppress("FunctionName")
@Dao
interface SequenceDao {
    @Query("SELECT * FROM sequence WHERE id = :id")
    suspend fun _getSequence(id: Uuid): SequenceEntity

    suspend fun getSequence(id: Uuid): SequenceWithSegments = SequenceWithSegments(
        sequenceEntity = _getSequence(id),
        segmentEntities = _getSequenceSegments(id),
    )

    @Query("SELECT * FROM sequence WHERE workspace_id = :workspaceId")
    fun _getSequences(workspaceId: Uuid): Flow<List<SequenceEntity>>

    @Query("SELECT * FROM segment WHERE sequence_id = :id ORDER BY list_order ASC")
    suspend fun _getSequenceSegments(id: Uuid): List<SegmentEntity>

    fun getSequences(workspaceId: Uuid): Flow<List<SequenceWithSegments>> =
        _getSequences(workspaceId).map { entities ->
            entities.map { entity ->
                SequenceWithSegments(
                    sequenceEntity = entity,
                    segmentEntities = _getSequenceSegments(entity.id),
                )
            }
        }

    @Upsert
    suspend fun upsertSequence(sequenceEntity: SequenceEntity)

    @Delete
    suspend fun delete(sequenceEntity: SequenceEntity)
}