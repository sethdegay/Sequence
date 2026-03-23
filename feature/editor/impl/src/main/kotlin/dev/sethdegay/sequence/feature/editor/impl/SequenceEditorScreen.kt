package dev.sethdegay.sequence.feature.editor.impl

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
import dev.sethdegay.sequence.core.designsystem.R.string.navigate_up_content_description
import dev.sethdegay.sequence.core.designsystem.component.CountdownDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIcon
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.ui.ReorderableCardGroup
import dev.sethdegay.sequence.feature.editor.api.SegmentEditorNav

@Composable
fun SequenceEditorScreen(
    viewModel: SequenceEditorViewModel,
    navigateToSegmentEditor: (SegmentEditorNav) -> Unit,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = SequenceIcons.NavigateUp.asComposableIconButton(
                    onClick = dropUnlessResumed { navigateUp() },
                    contentDescription = stringResource(navigate_up_content_description),
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val key = uiState.sequence?.let { sequence ->
                        val segments = sequence.segments
                        SegmentEditorNav.Create(
                            sequenceId = sequence.id,
                            lastSegmentPosition =
                                if (segments.isNotEmpty()) segments.last().order else 0,
                        )
                    }
                    if (key != null) navigateToSegmentEditor(key) // TODO handle null
                },
                content = SequenceIcons.Add.asComposableIcon(),
            )
        }
    ) { padding ->
        uiState.let { uiState ->
            when (uiState) {
                is SequenceEditorUiState.Loading ->
                    LoadingScreen(modifier = Modifier.padding(padding))

                is SequenceEditorUiState.Success -> {
                    SequenceEditorScreen(
                        scaffoldPadding = padding,
                        sequence = uiState.sequence,
                        onTitleSave = viewModel::onTitleSave,
                        onDescriptionSave = viewModel::onDescriptionSave,
                        onSegmentOrderChanged = viewModel::onSegmentsSave,
                        onSegmentClick = { segment ->
                            val key = uiState.sequence.let { sequence ->
                                SegmentEditorNav.Edit(
                                    segmentId = segment.id,
                                    sequenceId = sequence.id,
                                )
                            }
                            navigateToSegmentEditor(key)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SequenceEditorScreen(
    scaffoldPadding: PaddingValues,
    sequence: Sequence,
    onTitleSave: (String) -> Unit,
    onDescriptionSave: (String) -> Unit,
    onSegmentOrderChanged: (List<Segment>) -> Unit,
    onSegmentClick: (Segment) -> Unit,
) {
    ReorderableCardGroup(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .padding(scaffoldPadding)
            .fillMaxWidth(),
        segments = sequence.segments,
        onSegmentOrderChanged = onSegmentOrderChanged,
        onSegmentClick = onSegmentClick,
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