package dev.sethdegay.routines.core.datastore

import androidx.datastore.core.DataStore
import dev.sethdegay.routines.core.model.Settings
import dev.sethdegay.routines.core.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import dev.sethdegay.routines.core.model.ThemeConfig as ModelThemeConfig
import dev.sethdegay.routines.core.model.UserPreferences as ModelUserPreferences

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
            ),
            uiState = UiState(
                routinesAccordionExpandedId = if (userPreferences.uiState.hasRoutinesAccordionExpandedId()) {
                    userPreferences.uiState.routinesAccordionExpandedId
                } else {
                    null
                },
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
}