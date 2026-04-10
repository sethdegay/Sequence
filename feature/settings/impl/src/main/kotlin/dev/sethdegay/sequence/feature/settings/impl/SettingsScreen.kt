package dev.sethdegay.sequence.feature.settings.impl

import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.dropUnlessResumed
import dev.sethdegay.sequence.core.designsystem.R.string.navigate_up_content_description
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.IconButton
import dev.sethdegay.sequence.core.model.ThemeConfig
import dev.sethdegay.sequence.core.ui.BooleanPreference
import dev.sethdegay.sequence.core.ui.PreferenceError
import dev.sethdegay.sequence.core.ui.TogglePreference
import dev.sethdegay.sequence.feature.settings.impl.R.string

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateToLicense: () -> Unit,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LifecycleResumeEffect(Unit) {
        viewModel.updateHasTtsEngineInstalled()
        onPauseOrDispose {}
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(string.settings_top_app_bar_title))
                },
                navigationIcon = {
                    SequenceIcons.NavigateUp.IconButton(
                        onClick = dropUnlessResumed { navigateUp() },
                        contentDescription = stringResource(navigate_up_content_description),
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { padding ->
        if (uiState.showLoadingScreen()) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else {
            SettingsScreen(
                scaffoldPadding = padding,
                navigateToLicense = navigateToLicense,
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
    navigateToLicense: () -> Unit,
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
                        onCheckedRequest = { uiState.themeConfig == it },
                    ) {
                        item(
                            value = ThemeConfig.FOLLOW_SYSTEM,
                            onValueChanged = setThemeConfig,
                            label = { stringResource(string.settings_system_theme_title) },
                            iconChecked = { SequenceIcons.SystemChecked },
                            iconUnchecked = { SequenceIcons.SystemUnchecked },
                        )
                        item(
                            value = ThemeConfig.LIGHT,
                            onValueChanged = setThemeConfig,
                            label = { stringResource(string.settings_light_theme_title) },
                            iconChecked = { SequenceIcons.LightModeChecked },
                            iconUnchecked = { SequenceIcons.LightModeUnchecked },
                        )
                        item(
                            value = ThemeConfig.DARK,
                            onValueChanged = setThemeConfig,
                            label = { stringResource(string.settings_dark_theme_title) },
                            iconChecked = { SequenceIcons.DarkModeChecked },
                            iconUnchecked = { SequenceIcons.DarkModeUnchecked },
                        )
                    }
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
                        checked = uiState.speakTitle && uiState.hasTtsEngineInstalled,
                        onCheckedChange = setSpeakTitle,
                        isEnabled = !uiState.muteAll && uiState.hasTtsEngineInstalled,
                        preferenceError = if (!uiState.hasTtsEngineInstalled) {
                            PreferenceError(message = stringResource(string.settings_no_tts_engine_found_error_message))
                        } else {
                            null
                        },
                    )
                }
            }
        }
        item {
            CardGroup {
                item {
                    ListItem(
                        headlineContent = { Text(stringResource(string.settings_version_title)) },
                        supportingContent = { Text(versionName) },
                        colors = ListItemDefaults.colors(
                            containerColor = CardDefaults.cardColors().containerColor,
                            headlineColor = CardDefaults.cardColors().contentColor,
                            supportingColor = CardDefaults.cardColors().contentColor,
                        ),
                    )
                }
                item {
                    ListItem(
                        modifier = Modifier.clickable(onClick = navigateToLicense),
                        headlineContent = { Text(stringResource(string.settings_open_source_licenses_title)) },
                        colors = ListItemDefaults.colors(
                            containerColor = CardDefaults.cardColors().containerColor,
                            headlineColor = CardDefaults.cardColors().contentColor,
                            supportingColor = CardDefaults.cardColors().contentColor,
                        ),
                    )
                }
            }
        }
        item {}
    }
}

private val versionName: String
    @Composable
    get() {
        val context = LocalContext.current
        val packageInfo = try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo?.versionName ?: stringResource(string.settings_version_not_available)
    }