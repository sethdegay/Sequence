package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.database.dao.WorkspaceDao
import dev.sethdegay.sequence.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.Workspace
import javax.inject.Inject

class LocalWorkspaceRepository @Inject constructor(
    private val workspaceDao: WorkspaceDao,
) : WorkspaceRepository {
    override suspend fun getWorkspaces(): List<Workspace> =
        workspaceDao.getWorkspaces().map { it.asExternalModel(emptyList()) }
}