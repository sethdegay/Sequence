package dev.sethdegay.routines.feature.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.CountdownDisplay
import dev.sethdegay.routines.core.designsystem.component.LoadingScreen
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIcon
import dev.sethdegay.routines.core.designsystem.util.asComposableIconButton
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.ui.ReorderableCardGroup
import dev.sethdegay.routines.core.ui.TaskEditorSheet

@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = RoutinesIcons.NavigateUp.asComposableIconButton(
                    onClick = navigateUp,
                    contentDescription = stringResource(string.navigate_up_content_description),
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showTaskEditor(null) },
                content = RoutinesIcons.Add.asComposableIcon(),
            )
        }
    ) { padding ->
        if (uiState.showLoadingScreen()) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else {
            when (uiState) {
                is EditorUiState.Success -> {
                    EditorScreen(
                        scaffoldPadding = padding,
                        uiState = uiState as EditorUiState.Success,
                        onRoutineTitleSave = viewModel::onRoutineTitleSave,
                        onRoutineDescriptionSave = viewModel::onRoutineDescriptionSave,
                        onTaskOrderChanged = viewModel::onTasksSave,
                        onTaskClick = viewModel::showTaskEditor,
                    )

                    if (uiState.showTaskEditorSheet) {
                        TaskEditorSheet(
                            task = uiState.activeTask,
                            onTaskSave = viewModel::onTaskSave,
                            onDismissRequest = viewModel::hideTaskEditor,
                        )
                    }
                }

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
    onRoutineTitleSave: (String) -> Unit,
    onRoutineDescriptionSave: (String) -> Unit,
    onTaskOrderChanged: (List<Task>) -> Unit,
    onTaskClick: (Task) -> Unit,
) {
    ReorderableCardGroup(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .padding(scaffoldPadding)
            .fillMaxWidth(),
        tasks = uiState.routine.tasks,
        onTaskOrderChanged = onTaskOrderChanged,
        onTaskClick = onTaskClick,
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.routine.title,
                onValueChange = onRoutineTitleSave,
                label = { Text("Title") },
                singleLine = true,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.routine.description,
                onValueChange = onRoutineDescriptionSave,
                label = { Text("Description") },
                singleLine = false,
                minLines = 3,
                maxLines = 10,
            )
            Spacer(Modifier.size(16.dp))
            CountdownDisplay(
                duration = uiState.routine.totalDuration,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}