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
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.CardGroup
import dev.sethdegay.routines.core.designsystem.component.LoadingScreen
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIconButton
import dev.sethdegay.routines.core.model.ThemeConfig
import dev.sethdegay.routines.core.ui.DynamicColorPreference
import dev.sethdegay.routines.core.ui.ThemePreference

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
                navigationIcon = RoutinesIcons.NavigateUp.asComposableIconButton(
                    onClick = navigateUp,
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
                item { padding ->
                    ThemePreference(
                        modifier = Modifier.padding(padding),
                        onCheckedRequest = { uiState.themeConfig == it },
                        onThemeConfigChanged = setThemeConfig,
                    )
                }
                item { padding ->
                    DynamicColorPreference(
                        modifier = Modifier.padding(padding),
                        checked = uiState.useDynamicColor,
                        onCheckedChange = setDynamicColor,
                    )
                }
            }
        }
        item {}
    }
}
