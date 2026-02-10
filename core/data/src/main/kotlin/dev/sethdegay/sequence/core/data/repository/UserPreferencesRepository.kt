package dev.sethdegay.sequence.core.data.repository

import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.model.ThemeConfig
import dev.sethdegay.sequence.core.model.UiState
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val settings: Flow<Settings>

    val uiState: Flow<UiState>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)

    suspend fun setDynamicColor(dynamicColor: Boolean)

    suspend fun setRoutinesAccordionExpandedId(routinesAccordionExpandedId: String?)

    suspend fun setMuteAll(muteAll: Boolean)

    suspend fun setTickSound(tickSound: Boolean)

    suspend fun setCompletionSound(completionSound: Boolean)

    suspend fun setSpeakTitle(speakTitle: Boolean)
}