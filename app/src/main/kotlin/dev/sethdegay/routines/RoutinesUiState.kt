package dev.sethdegay.routines

import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.model.UserPreferences

sealed interface RoutinesUiState {
    data object Loading : RoutinesUiState

    data class Success(val userPreferences: UserPreferences) : RoutinesUiState {
        override fun useDarkTheme(isSystemInDarkTheme: Boolean): Boolean =
            when (userPreferences.themeConfig) {
                ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme
                ThemeConfig.LIGHT -> false
                ThemeConfig.DARK -> true
            }

        override val useDynamicColor: Boolean = userPreferences.dynamicColor
    }

    fun showSplashScreen(): Boolean = this is Loading

    fun useDarkTheme(isSystemInDarkTheme: Boolean): Boolean = isSystemInDarkTheme

    val useDynamicColor: Boolean
        get() = false
}