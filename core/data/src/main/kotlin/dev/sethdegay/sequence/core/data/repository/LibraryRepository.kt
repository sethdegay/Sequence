package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Library
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getOldestLibrary(): Flow<Library?>
}