package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.datastore.UserPreferencesDataSource
import dev.sethdegay.routines.core.model.Settings
import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserPreferencesRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {
    override val settings: Flow<Settings> = dataSource.userPreferences
        .map { it.settings }
        .distinctUntilChanged()

    override val uiState: Flow<UiState> = dataSource.userPreferences
        .map { it.uiState }
        .distinctUntilChanged()

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        dataSource.setThemeConfig(themeConfig)
    }

    override suspend fun setDynamicColor(dynamicColor: Boolean) {
        dataSource.setDynamicColor(dynamicColor)
    }
}