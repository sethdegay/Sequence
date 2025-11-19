package dev.sethdegay.routines.core.datastore

import androidx.datastore.core.DataStore
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
            themeConfig = when (userPreferences.themeConfig) {
                null,
                ThemeConfig.UNRECOGNIZED,
                ThemeConfig.UNSPECIFIED,
                ThemeConfig.FOLLOW_SYSTEM -> ModelThemeConfig.FOLLOW_SYSTEM

                ThemeConfig.LIGHT -> ModelThemeConfig.LIGHT
                ThemeConfig.DARK -> ModelThemeConfig.DARK
            },
            dynamicColor = userPreferences.dynamicColor,
        )
    }

    suspend fun setThemeConfig(themeConfig: ModelThemeConfig) {
        _userPreferences.updateData {
            it.copy {
                this.themeConfig = when (themeConfig) {
                    ModelThemeConfig.FOLLOW_SYSTEM -> ThemeConfig.FOLLOW_SYSTEM
                    ModelThemeConfig.LIGHT -> ThemeConfig.LIGHT
                    ModelThemeConfig.DARK -> ThemeConfig.DARK
                }
            }
        }
    }

    suspend fun setDynamicColor(dynamicColor: Boolean) {
        _userPreferences.updateData {
            it.copy {
                this.dynamicColor = dynamicColor
            }
        }
    }
}