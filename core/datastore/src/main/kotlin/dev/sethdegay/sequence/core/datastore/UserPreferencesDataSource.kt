package dev.sethdegay.sequence.core.datastore

import androidx.datastore.core.DataStore
import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import dev.sethdegay.sequence.core.model.ThemeConfig as ExtThemeConfig
import dev.sethdegay.sequence.core.model.UserPreferences as ExtUserPreferences

class UserPreferencesDataSource @Inject constructor(
    private val _userPreferences: DataStore<UserPreferences>,
) {
    val userPreferences: Flow<ExtUserPreferences> =
        _userPreferences.data.map { it.asExternalModel() }

    suspend fun setThemeConfig(themeConfig: ExtThemeConfig) {
        _userPreferences.updateData { current ->
            current.copy { settings = settings.copy { this.themeConfig = themeConfig.asProto() } }
        }
    }

    suspend fun setDynamicColor(dynamicColor: Boolean) {
        _userPreferences.updateData { current ->
            current.copy { settings = settings.copy { this.dynamicColor = dynamicColor } }
        }
    }

    suspend fun setMuteAll(muteAll: Boolean) {
        _userPreferences.updateData { current ->
            current.copy { settings = settings.copy { this.muteAll = muteAll } }
        }
    }

    suspend fun setTickSound(tickSound: Boolean) {
        _userPreferences.updateData { current ->
            current.copy { settings = settings.copy { this.tickSound = tickSound } }
        }
    }

    suspend fun setCompletionSound(completionSound: Boolean) {
        _userPreferences.updateData { current ->
            current.copy { settings = settings.copy { this.completionSound = completionSound } }
        }
    }

    suspend fun setSpeakTitle(speakTitle: Boolean) {
        _userPreferences.updateData { current ->
            current.copy { settings = settings.copy { this.speakTitle = speakTitle } }
        }
    }

    suspend fun setAccordionExpandedId(accordionExpandedId: String?) {
        _userPreferences.updateData { current ->
            current.copy {
                this.uiState = this.uiState.copy {
                    if (accordionExpandedId == null) {
                        clearAccordionExpandedId()
                    } else {
                        this.accordionExpandedId = accordionExpandedId
                    }
                }
            }
        }
    }
}

private fun UserPreferences.asExternalModel(): ExtUserPreferences {
    return ExtUserPreferences(
        settings = Settings(
            themeConfig = settings.themeConfig.asExternalModel(),
            dynamicColor = settings.dynamicColor,
            muteAll = settings.muteAll,
            tickSound = settings.tickSound,
            completionSound = settings.completionSound,
            speakTitle = settings.speakTitle,
        ),
        uiState = UiState(
            accordionExpandedId = uiState.accordionExpandedId.takeIf {
                uiState.hasAccordionExpandedId() && it.isValidV4Uuid()
            },
        ),
    )
}

private fun ThemeConfig.asExternalModel(): ExtThemeConfig = when (this) {
    ThemeConfig.LIGHT -> ExtThemeConfig.LIGHT
    ThemeConfig.DARK -> ExtThemeConfig.DARK
    else -> ExtThemeConfig.FOLLOW_SYSTEM
}

private fun ExtThemeConfig.asProto(): ThemeConfig = when (this) {
    ExtThemeConfig.FOLLOW_SYSTEM -> ThemeConfig.FOLLOW_SYSTEM
    ExtThemeConfig.LIGHT -> ThemeConfig.LIGHT
    ExtThemeConfig.DARK -> ThemeConfig.DARK
}

private val V4_UUID_REGEX =
    "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$".toRegex()

private fun String.isValidV4Uuid(): Boolean = matches(V4_UUID_REGEX)