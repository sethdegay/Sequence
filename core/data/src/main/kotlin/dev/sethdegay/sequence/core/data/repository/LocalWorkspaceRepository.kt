package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.database.dao.WorkspaceDao
import dev.sethdegay.sequence.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.Workspace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalWorkspaceRepository @Inject constructor(
    private val workspaceDao: WorkspaceDao,
) : WorkspaceRepository {
    override fun getOldestWorkspace(): Flow<Workspace?> =
        workspaceDao.getOldestWorkspace().map { it?.asExternalModel(emptyList()) }
}