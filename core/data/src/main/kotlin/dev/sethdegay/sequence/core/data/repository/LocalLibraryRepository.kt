package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.database.dao.LibraryDao
import dev.sethdegay.sequence.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.Library
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalLibraryRepository @Inject constructor(
    private val libraryDao: LibraryDao,
) : LibraryRepository {
    override fun getOldestLibrary(): Flow<Library?> =
        libraryDao.getOldestLibrary().map { it?.asExternalModel(emptyList()) }
}