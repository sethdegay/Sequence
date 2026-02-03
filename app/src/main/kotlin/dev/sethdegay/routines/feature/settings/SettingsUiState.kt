package dev.sethdegay.routines.feature.settings

import dev.sethdegay.routines.core.model.Settings
import dev.sethdegay.routines.core.model.ThemeConfig

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(val settings: Settings) : SettingsUiState {
        override val themeConfig: ThemeConfig = settings.themeConfig
        override val dynamicColor: Boolean = settings.dynamicColor
        override val muteAll: Boolean = settings.muteAll
        override val tickSound: Boolean = settings.tickSound
        override val completionSound: Boolean = settings.completionSound
        override val speakTitle: Boolean = settings.speakTitle
    }

    fun showLoadingScreen(): Boolean = this is Loading

    val themeConfig: ThemeConfig
        get() = ThemeConfig.FOLLOW_SYSTEM

    val dynamicColor: Boolean
        get() = false

    val muteAll: Boolean
        get() = false

    val tickSound: Boolean
        get() = true

    val completionSound: Boolean
        get() = true

    val speakTitle: Boolean
        get() = true
}