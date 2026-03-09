package dev.sethdegay.sequence.core.model

import kotlin.uuid.Uuid

data class UserPreferences(
    val settings: Settings,
    val uiState: UiState,
)

data class Settings(
    val themeConfig: ThemeConfig,
    val dynamicColor: Boolean,
    val muteAll: Boolean,
    val tickSound: Boolean,
    val completionSound: Boolean,
    val speakTitle: Boolean,
)

data class UiState(
    val accordionExpandedId: Uuid?,
)

enum class ThemeConfig { FOLLOW_SYSTEM, LIGHT, DARK; }