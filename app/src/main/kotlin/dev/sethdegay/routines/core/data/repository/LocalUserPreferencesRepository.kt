package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.datastore.UserPreferencesDataSource
import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalUserPreferencesRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {
    override val userPreferences: Flow<UserPreferences> = dataSource.userPreferences

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        dataSource.setThemeConfig(themeConfig)
    }

    override suspend fun setDynamicColor(dynamicColor: Boolean) {
        dataSource.setDynamicColor(dynamicColor)
    }
}