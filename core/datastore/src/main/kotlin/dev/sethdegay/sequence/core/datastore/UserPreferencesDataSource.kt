package dev.sethdegay.sequence.core.datastore

import androidx.datastore.core.DataStore
import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import dev.sethdegay.sequence.core.model.ThemeConfig as ModelThemeConfig
import dev.sethdegay.sequence.core.model.UserPreferences as ModelUserPreferences

class UserPreferencesDataSource @Inject constructor(
    private val _userPreferences: DataStore<UserPreferences>,
) {
    val userPreferences: Flow<ModelUserPreferences> = _userPreferences.data.map { userPreferences ->
        ModelUserPreferences(
            settings = Settings(
                themeConfig = when (userPreferences.settings.themeConfig) {
                    null,
                    ThemeConfig.UNRECOGNIZED,
                    ThemeConfig.UNSPECIFIED,
                    ThemeConfig.FOLLOW_SYSTEM -> ModelThemeConfig.FOLLOW_SYSTEM

                    ThemeConfig.LIGHT -> ModelThemeConfig.LIGHT
                    ThemeConfig.DARK -> ModelThemeConfig.DARK
                },
                dynamicColor = userPreferences.settings.dynamicColor,
                muteAll = userPreferences.settings.muteAll,
                tickSound = userPreferences.settings.tickSound,
                completionSound = userPreferences.settings.completionSound,
                speakTitle = userPreferences.settings.speakTitle,
            ),
            uiState = UiState(
                routinesAccordionExpandedId = userPreferences.uiState.routinesAccordionExpandedId
                    .takeIf { userPreferences.uiState.hasRoutinesAccordionExpandedId() && it.isNotEmpty() },
            )
        )
    }

    suspend fun setThemeConfig(themeConfig: ModelThemeConfig) {
        _userPreferences.updateData { current ->
            current.copy {
                this.settings = this.settings.copy {
                    this.themeConfig = when (themeConfig) {
                        ModelThemeConfig.FOLLOW_SYSTEM -> ThemeConfig.FOLLOW_SYSTEM
                        ModelThemeConfig.LIGHT -> ThemeConfig.LIGHT
                        ModelThemeConfig.DARK -> ThemeConfig.DARK
                    }
                }
            }
        }
    }

    suspend fun setDynamicColor(dynamicColor: Boolean) {
        _userPreferences.updateData { current ->
            current.copy {
                this.settings = this.settings.copy {
                    this.dynamicColor = dynamicColor
                }
            }
        }
    }

    suspend fun setRoutinesAccordionExpandedId(routinesAccordionExpandedId: String?) {
        _userPreferences.updateData { current ->
            val uiStateBuilder = current.uiState.toBuilder()
            if (routinesAccordionExpandedId == null) {
                uiStateBuilder.clearRoutinesAccordionExpandedId()
            } else {
                uiStateBuilder.setRoutinesAccordionExpandedId(routinesAccordionExpandedId)
            }
            current.toBuilder().setUiState(uiStateBuilder).build()
        }
    }

    suspend fun setMuteAll(muteAll: Boolean) {
        _userPreferences.updateData { current ->
            current.copy {
                this.settings = this.settings.copy {
                    this.muteAll = muteAll
                }
            }
        }
    }

    suspend fun setTickSound(tickSound: Boolean) {
        _userPreferences.updateData { current ->
            current.copy {
                this.settings = this.settings.copy {
                    this.tickSound = tickSound
                }
            }
        }
    }

    suspend fun setCompletionSound(completionSound: Boolean) {
        _userPreferences.updateData { current ->
            current.copy {
                this.settings = this.settings.copy {
                    this.completionSound = completionSound
                }
            }
        }
    }

    suspend fun setSpeakTitle(speakTitle: Boolean) {
        _userPreferences.updateData { current ->
            current.copy {
                this.settings = this.settings.copy {
                    this.speakTitle = speakTitle
                }
            }
        }
    }
}