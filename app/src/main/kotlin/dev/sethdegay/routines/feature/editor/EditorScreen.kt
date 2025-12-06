package dev.sethdegay.routines.feature.editor

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.LoadingScreen
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIconButton
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.ui.ReorderableCardGroup

@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {},
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
            when (uiState) {
                is EditorUiState.Success -> EditorScreen(
                    scaffoldPadding = padding,
                    uiState = uiState as EditorUiState.Success,
                    onTaskOrderChanged = { viewModel.setTasks(it) },
                )

                else -> Text(
                    modifier = Modifier.padding(padding),
                    text = uiState.toString(),
                )
            }
        }
    }
}

@Composable
private fun EditorScreen(
    scaffoldPadding: PaddingValues,
    uiState: EditorUiState.Success,
    onTaskOrderChanged: (List<Task>) -> Unit,
) {
    ReorderableCardGroup(
        modifier = Modifier.padding(scaffoldPadding),
        tasks = uiState.routine.tasks,
        onTaskOrderChanged = onTaskOrderChanged,
        onTaskClick = { },
    )
}