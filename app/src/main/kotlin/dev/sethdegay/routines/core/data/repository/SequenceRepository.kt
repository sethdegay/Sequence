package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.flow.Flow

interface SequenceRepository {
    suspend fun getSequence(id: String): Sequence

    fun getSequences(): Flow<List<Sequence>>

    suspend fun saveSequence(sequence: Sequence)

    suspend fun delete(sequence: Sequence)
}