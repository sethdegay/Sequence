package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Library
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface LibraryRepository {
    fun getOldestLibrary(): Flow<Library?>
    fun getLibraryTitle(id: Uuid): Flow<String>
}