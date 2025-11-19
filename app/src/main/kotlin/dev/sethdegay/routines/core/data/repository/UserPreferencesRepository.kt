package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)

    suspend fun setDynamicColor(dynamicColor: Boolean)
}