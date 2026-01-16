package dev.sethdegay.routines

import dev.sethdegay.routines.core.model.Settings
import dev.sethdegay.routines.core.model.ThemeConfig

sealed interface RoutinesUiState {
    data object Loading : RoutinesUiState

    data class Success(val settings: Settings) : RoutinesUiState {
        override fun useDarkTheme(isSystemInDarkTheme: Boolean): Boolean =
            when (settings.themeConfig) {
                ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme
                ThemeConfig.LIGHT -> false
                ThemeConfig.DARK -> true
            }

        override val useDynamicColor: Boolean = settings.dynamicColor
    }

    fun showSplashScreen(): Boolean = this is Loading

    fun useDarkTheme(isSystemInDarkTheme: Boolean): Boolean = isSystemInDarkTheme

    val useDynamicColor: Boolean
        get() = false
}