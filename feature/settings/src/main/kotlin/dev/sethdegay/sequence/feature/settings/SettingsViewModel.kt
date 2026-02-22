package dev.sethdegay.sequence.feature.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.sethdegay.sequence.core.audio.TtsManager
import dev.sethdegay.sequence.core.data.repository.UserPreferencesRepository
import dev.sethdegay.sequence.core.model.ThemeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {
    private val _hasTtsEngineInstalled = MutableStateFlow(true)

    val uiState: StateFlow<SettingsUiState> =
        combine(
            userPreferencesRepository.settings,
            _hasTtsEngineInstalled,
        ) { settings, hasTtsEngineInstalled ->
            SettingsUiState.Success(
                settings = settings,
                hasTtsEngineInstalled = hasTtsEngineInstalled,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading,
        )

    fun setThemeConfig(themeConfig: ThemeConfig) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeConfig(themeConfig)
        }
    }

    fun setDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDynamicColor(dynamicColor)
        }
    }

    fun setMuteAll(muteAll: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setMuteAll(muteAll)
        }
    }

    fun setTickSound(tickSound: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setTickSound(tickSound)
        }
    }

    fun setCompletionSound(completionSound: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setCompletionSound(completionSound)
        }
    }

    fun setSpeakTitle(speakTitle: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setSpeakTitle(speakTitle)
        }
    }

    fun updateHasTtsEngineInstalled() {
        _hasTtsEngineInstalled.update { TtsManager.hasTtsEngineInstalled(context) }
    }
}