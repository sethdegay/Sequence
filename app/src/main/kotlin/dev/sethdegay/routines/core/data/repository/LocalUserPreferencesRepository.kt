package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.sequence.core.datastore.UserPreferencesDataSource
import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.model.ThemeConfig
import dev.sethdegay.sequence.core.model.UiState
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

    override suspend fun setRoutinesAccordionExpandedId(routinesAccordionExpandedId: String?) {
        dataSource.setRoutinesAccordionExpandedId(routinesAccordionExpandedId)
    }

    override suspend fun setMuteAll(muteAll: Boolean) {
        dataSource.setMuteAll(muteAll)
    }

    override suspend fun setTickSound(tickSound: Boolean) {
        dataSource.setTickSound(tickSound)
    }

    override suspend fun setCompletionSound(completionSound: Boolean) {
        dataSource.setCompletionSound(completionSound)
    }

    override suspend fun setSpeakTitle(speakTitle: Boolean) {
        dataSource.setSpeakTitle(speakTitle)
    }
}