@file:OptIn(ExperimentalUuidApi::class)

package dev.sethdegay.sequence.core.datastore

import androidx.datastore.core.DataStore
import com.google.protobuf.ByteString
import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import dev.sethdegay.sequence.core.model.SegmentInputMethod as ExtSegmentInputMethod
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

    suspend fun setActiveLibraryId(activeLibraryId: Uuid?) {
        _userPreferences.updateData { current ->
            current.copy {
                uiState = uiState.copy {
                    if (activeLibraryId == null) {
                        clearActiveLibraryId()
                    } else {
                        this.activeLibraryId = ByteString.copyFrom(activeLibraryId.toByteArray())
                    }
                }
            }
        }
    }

    suspend fun setActiveSequenceId(activeSequenceId: Uuid?) {
        _userPreferences.updateData { current ->
            current.copy {
                uiState = uiState.copy {
                    if (activeSequenceId == null) {
                        clearActiveSequenceId()
                    } else {
                        this.activeSequenceId = ByteString.copyFrom(activeSequenceId.toByteArray())
                    }
                }
            }
        }
    }

    suspend fun setActiveSegmentIm(activeSegmentIm: ExtSegmentInputMethod) {
        _userPreferences.updateData { current ->
            current.copy {
                uiState = uiState.copy {
                    this.activeSegmentIm = when (activeSegmentIm) {
                        ExtSegmentInputMethod.PICK -> SegmentInputMethod.PICK
                        ExtSegmentInputMethod.TYPE -> SegmentInputMethod.TYPE
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
            activeLibraryId = uiState.activeLibraryId.toUuidOrNull(),
            activeSequenceId = uiState.activeSequenceId.toUuidOrNull(),
            activeSegmentIm = when (uiState.activeSegmentIm) {
                SegmentInputMethod.PICK, SegmentInputMethod.UNRECOGNIZED -> ExtSegmentInputMethod.PICK
                SegmentInputMethod.TYPE -> ExtSegmentInputMethod.TYPE
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

private fun ByteString.toUuidOrNull(): Uuid? {
    return if (isEmpty) null else {
        try {
            Uuid.fromByteArray(toByteArray())
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}