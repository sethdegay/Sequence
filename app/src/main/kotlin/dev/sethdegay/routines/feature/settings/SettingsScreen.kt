package dev.sethdegay.routines.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import dev.sethdegay.routines.R.string
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.component.ToggleButtonOption
import dev.sethdegay.sequence.core.designsystem.component.ToggleButtonPosition
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton
import dev.sethdegay.sequence.core.model.ThemeConfig
import dev.sethdegay.sequence.core.ui.BooleanPreference
import dev.sethdegay.sequence.core.ui.TogglePreference

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(string.settings_top_app_bar_title))
                },
                navigationIcon = SequenceIcons.NavigateUp.asComposableIconButton(
                    onClick = dropUnlessResumed { navigateUp() },
                    contentDescription = stringResource(string.navigate_up_content_description),
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { padding ->
        if (uiState.showLoadingScreen()) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else {
            SettingsScreen(
                scaffoldPadding = padding,
                uiState = uiState,
                setThemeConfig = viewModel::setThemeConfig,
                setDynamicColor = viewModel::setDynamicColor,
                setMuteAll = viewModel::setMuteAll,
                setTickSound = viewModel::setTickSound,
                setCompletionSound = viewModel::setCompletionSound,
                setSpeakTitle = viewModel::setSpeakTitle,
            )
        }
    }
}

@Composable
private fun SettingsScreen(
    scaffoldPadding: PaddingValues,
    uiState: SettingsUiState,
    setThemeConfig: (ThemeConfig) -> Unit,
    setDynamicColor: (Boolean) -> Unit,
    setMuteAll: (Boolean) -> Unit,
    setTickSound: (Boolean) -> Unit,
    setCompletionSound: (Boolean) -> Unit,
    setSpeakTitle: (Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .fillMaxSize(),
        contentPadding = scaffoldPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {}
        item {
            CardGroup {
                item {
                    TogglePreference(
                        title = stringResource(string.settings_theme_title),
                        description = stringResource(string.settings_theme_description),
                        options = {
                            listOf(
                                ToggleButtonOption(
                                    label = stringResource(string.settings_system_theme_title),
                                    iconChecked = SequenceIcons.SystemChecked,
                                    iconUnchecked = SequenceIcons.SystemUnchecked,
                                    value = ThemeConfig.FOLLOW_SYSTEM,
                                    onValueChanged = setThemeConfig,
                                    toggleButtonPosition = ToggleButtonPosition.LEADING,
                                ),
                                ToggleButtonOption(
                                    label = stringResource(string.settings_light_theme_title),
                                    iconChecked = SequenceIcons.LightModeChecked,
                                    iconUnchecked = SequenceIcons.LightModeUnchecked,
                                    value = ThemeConfig.LIGHT,
                                    onValueChanged = setThemeConfig,
                                    toggleButtonPosition = ToggleButtonPosition.MIDDLE,
                                ),
                                ToggleButtonOption(
                                    label = stringResource(string.settings_dark_theme_title),
                                    iconChecked = SequenceIcons.DarkModeChecked,
                                    iconUnchecked = SequenceIcons.DarkModeUnchecked,
                                    value = ThemeConfig.DARK,
                                    onValueChanged = setThemeConfig,
                                    toggleButtonPosition = ToggleButtonPosition.TRAILING,
                                ),
                            )
                        },
                        onCheckedRequest = { uiState.themeConfig == it },
                    )
                }
                item {
                    BooleanPreference(
                        title = stringResource(string.settings_dynamic_color_title),
                        description = stringResource(string.settings_dynamic_color_description),
                        checked = uiState.dynamicColor,
                        onCheckedChange = setDynamicColor,
                    )
                }
            }
        }
        item {
            CardGroup {
                item {
                    BooleanPreference(
                        title = stringResource(string.settings_mute_all_title),
                        description = null,
                        checked = uiState.muteAll,
                        onCheckedChange = setMuteAll,
                    )
                }
                item {
                    BooleanPreference(
                        title = stringResource(string.settings_tick_sound_title),
                        description = stringResource(string.settings_tick_sound_description),
                        checked = uiState.tickSound,
                        onCheckedChange = setTickSound,
                        isEnabled = !uiState.muteAll,
                    )
                }
                item {
                    BooleanPreference(
                        title = stringResource(string.settings_completion_sound_title),
                        description = stringResource(string.settings_completion_sound_description),
                        checked = uiState.completionSound,
                        onCheckedChange = setCompletionSound,
                        isEnabled = !uiState.muteAll,
                    )
                }
                item {
                    BooleanPreference(
                        title = stringResource(string.settings_speak_title_title),
                        description = stringResource(string.settings_speak_title_description),
                        checked = uiState.speakTitle,
                        onCheckedChange = setSpeakTitle,
                        isEnabled = !uiState.muteAll,
                    )
                }
            }
        }
        item {}
    }
}
