package dev.sethdegay.routines.feature.settings

import dev.sethdegay.routines.core.model.Settings
import dev.sethdegay.routines.core.model.ThemeConfig

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(val settings: Settings) : SettingsUiState {
        override val themeConfig: ThemeConfig = settings.themeConfig

        override val useDynamicColor: Boolean = settings.dynamicColor
    }

    fun showLoadingScreen(): Boolean = this is Loading

    val themeConfig: ThemeConfig
        get() = ThemeConfig.FOLLOW_SYSTEM

    val useDynamicColor: Boolean
        get() = false
}