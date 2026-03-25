package dev.sethdegay.sequence.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.sethdegay.sequence.core.database.model.WorkspaceEntity
import dev.sethdegay.sequence.core.database.model.WorkspaceWithSequences
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM workspace ORDER BY date_created ASC LIMIT 1")
    fun getOldestWorkspace(): Flow<WorkspaceEntity?>

    @Query("SELECT * FROM workspace")
    suspend fun getWorkspaces(): List<WorkspaceEntity>

    @Transaction
    @Query("SELECT * FROM workspace WHERE id = :id")
    suspend fun getWorkspace(id: Uuid): WorkspaceWithSequences

    @Insert
    suspend fun insert(workspaceEntity: WorkspaceEntity)

    @Upsert
    suspend fun upsert(workspaceEntity: WorkspaceEntity)

    @Query("SELECT COUNT(*) FROM workspace")
    suspend fun getCount(): Int

    @Delete
    suspend fun deleteWorkspace(workspaceEntity: WorkspaceEntity)
}