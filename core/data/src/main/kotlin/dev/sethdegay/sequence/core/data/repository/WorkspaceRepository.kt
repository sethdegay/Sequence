package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Workspace
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {
    fun getOldestWorkspace(): Flow<Workspace?>
}