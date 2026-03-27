package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface SequenceRepository {
    suspend fun getSequence(id: Uuid): Sequence

    fun getSequences(libraryId: Uuid): Flow<List<Sequence>>

    suspend fun saveSequence(sequence: Sequence, libraryId: Uuid)

    suspend fun delete(sequence: Sequence, libraryId: Uuid)
}