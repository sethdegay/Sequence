package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.model.Settings
import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.model.UiState
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val settings: Flow<Settings>

    val uiState: Flow<UiState>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)

    suspend fun setDynamicColor(dynamicColor: Boolean)

    suspend fun setRoutinesAccordionExpandedId(routinesAccordionExpandedId: String?)
}