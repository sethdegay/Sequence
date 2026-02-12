package dev.sethdegay.sequence.feature.editor

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
import androidx.lifecycle.compose.dropUnlessResumed
import dev.sethdegay.sequence.core.designsystem.component.CountdownDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIcon
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Step
import dev.sethdegay.sequence.core.ui.ReorderableCardGroup
import dev.sethdegay.sequence.core.ui.StepEditorSheet
import dev.sethdegay.sequence.feature.editor.R.string

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
                navigationIcon = SequenceIcons.NavigateUp.asComposableIconButton(
                    onClick = dropUnlessResumed { navigateUp() },
                    contentDescription = stringResource(string.navigate_up_content_description),
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showStepEditor(null) },
                content = SequenceIcons.Add.asComposableIcon(),
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
                        sequence = uiState.sequence!!,
                        onTitleSave = viewModel::onTitleSave,
                        onDescriptionSave = viewModel::onDescriptionSave,
                        onStepOrderChanged = viewModel::onStepsSave,
                        onStepClick = viewModel::showStepEditor,
                    )

                    if (uiState.showStepEditorSheet) {
                        StepEditorSheet(
                            step = uiState.activeStep,
                            onStepSave = viewModel::onStepSave,
                            onDismissRequest = viewModel::hideStepEditor,
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
    sequence: Sequence,
    onTitleSave: (String) -> Unit,
    onDescriptionSave: (String) -> Unit,
    onStepOrderChanged: (List<Step>) -> Unit,
    onStepClick: (Step) -> Unit,
) {
    ReorderableCardGroup(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .padding(scaffoldPadding)
            .fillMaxWidth(),
        steps = sequence.steps,
        onStepOrderChanged = onStepOrderChanged,
        onStepClick = onStepClick,
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = sequence.title,
                onValueChange = onTitleSave,
                label = { Text("Title") },
                singleLine = true,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = sequence.description,
                onValueChange = onDescriptionSave,
                label = { Text("Description") },
                singleLine = false,
                minLines = 3,
                maxLines = 10,
            )
            Spacer(Modifier.size(16.dp))
            CountdownDisplay(
                duration = sequence.totalDuration,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}