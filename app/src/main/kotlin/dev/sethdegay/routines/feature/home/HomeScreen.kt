package dev.sethdegay.routines.feature.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIconButton

@Composable
fun HomeScreen(
    navigateToEditor: (String?) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTimer: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(string.app_name))
                },
                actions = {
                    RoutinesIcons.Settings.asComposableIconButton(
                        onClick = navigateToSettings,
                        contentDescription = stringResource(string.home_navigate_to_settings_content_description),
                    ).invoke()
                }
            )
        },
        floatingActionButton = {
            MediumExtendedFloatingActionButton(
                onClick = { navigateToEditor(null) }
            ) {
                Text(text = stringResource(string.home_add_routine_button_text))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .consumeWindowInsets(padding)
                .fillMaxSize(),
            contentPadding = padding,
        ) {
            item {
                Button(onClick = { navigateToTimer("") }) {
                    Text("Open Timer screen")
                }
            }
        }
    }
}