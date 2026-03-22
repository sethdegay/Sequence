package dev.sethdegay.sequence.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.sethdegay.sequence.core.database.model.SegmentEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface SegmentDao {
    @Query("SELECT * FROM segment WHERE sequence_id = :sequenceId")
    fun getSegments(sequenceId: Uuid): Flow<List<SegmentEntity>>

    @Query("SELECT * FROM segment WHERE id = :id")
    fun getSegment(id: Uuid): Flow<SegmentEntity>

    @Upsert
    suspend fun upsertSegment(segment: SegmentEntity)

    @Delete
    suspend fun deleteSegment(segment: SegmentEntity)
}