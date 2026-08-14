package dev.sethdegay.sequence.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.sethdegay.sequence.core.database.model.LibraryEntity
import dev.sethdegay.sequence.core.database.model.LibraryWithSequences
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library ORDER BY date_created ASC LIMIT 1")
    fun getOldestLibrary(): Flow<LibraryEntity?>

    @Query("SELECT * FROM library")
    suspend fun getLibraries(): List<LibraryEntity>

    @Transaction
    @Query("SELECT * FROM library WHERE id = :id")
    suspend fun getLibrary(id: Uuid): LibraryWithSequences

    @Query("SELECT title FROM library WHERE id = :id")
    fun getLibraryTitle(id: Uuid): Flow<String>

    @Insert
    suspend fun insert(libraryEntity: LibraryEntity)

    @Upsert
    suspend fun upsert(libraryEntity: LibraryEntity)

    @Query("SELECT COUNT(*) FROM library")
    suspend fun getCount(): Int

    @Delete
    suspend fun deleteLibrary(libraryEntity: LibraryEntity)
}