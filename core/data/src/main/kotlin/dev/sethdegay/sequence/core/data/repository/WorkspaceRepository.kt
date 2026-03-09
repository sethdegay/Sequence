package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Workspace

interface WorkspaceRepository {
    suspend fun getWorkspaces(): List<Workspace>
}