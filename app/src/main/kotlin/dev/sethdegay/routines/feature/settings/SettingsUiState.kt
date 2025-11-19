package dev.sethdegay.routines.feature.settings

import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.model.UserPreferences

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(val userPreferences: UserPreferences) : SettingsUiState {
        override val themeConfig: ThemeConfig = userPreferences.themeConfig

        override val useDynamicColor: Boolean = userPreferences.dynamicColor
    }

    fun showLoadingScreen(): Boolean = this is Loading

    val themeConfig: ThemeConfig
        get() = ThemeConfig.FOLLOW_SYSTEM

    val useDynamicColor: Boolean
        get() = false
}