package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface SequenceRepository {
    suspend fun getSequence(id: Uuid): Sequence

    fun getSequences(): Flow<List<Sequence>>

    suspend fun saveSequence(sequence: Sequence, workspaceId: Uuid)

    suspend fun delete(sequence: Sequence, workspaceId: Uuid)
}