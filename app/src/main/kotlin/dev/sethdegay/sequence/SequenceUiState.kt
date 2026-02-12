package dev.sethdegay.sequence

import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.model.ThemeConfig

sealed interface SequenceUiState {
    data object Loading : SequenceUiState

    data class Success(val settings: Settings) : SequenceUiState {
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