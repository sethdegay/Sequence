package dev.sethdegay.sequence.feature.editor_sequence.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import dev.sethdegay.sequence.core.designsystem.R.string.navigate_up_content_description
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.component.CountdownDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIcon
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.ui.ReorderableCardGroup
import dev.sethdegay.sequence.feature.editor_segment.api.SegmentEditorNav
import kotlin.time.Duration

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
                    onClick = dropUnlessResumed { viewModel.requestExit() },
                    contentDescription = stringResource(navigate_up_content_description),
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val key = uiState.segments?.let { segments ->
                        SegmentEditorNav.Create(
                            sequenceId = viewModel.getSequenceId(),
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
                        title = uiState.title,
                        description = uiState.description,
                        segments = uiState.segments,
                        totalDuration = uiState.totalDuration,
                        onSegmentOrderChanged = viewModel::onSegmentOrderChanged,
                        onSegmentClick = { segment ->
                            val key = SegmentEditorNav.Edit(
                                segmentId = segment.id,
                                sequenceId = viewModel.getSequenceId(),
                            )
                            navigateToSegmentEditor(key)
                        },
                    )
                }
            }
        }
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect {
            when (it) {
                is SequenceEditorEffect.Finished -> navigateUp()
            }
        }
    }

    BackHandler { viewModel.requestExit() }
}

@Composable
private fun SequenceEditorScreen(
    scaffoldPadding: PaddingValues,
    title: TextFieldState,
    description: TextFieldState,
    segments: List<Segment>,
    totalDuration: Duration,
    onSegmentOrderChanged: (List<Segment>) -> Unit,
    onSegmentClick: (Segment) -> Unit,
) {
    ReorderableCardGroup(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .padding(scaffoldPadding)
            .fillMaxWidth(),
        segments = segments,
        onSegmentOrderChanged = onSegmentOrderChanged,
        onSegmentClick = onSegmentClick,
    ) {
        Spacer(Modifier.size(16.dp))
        CardGroup {
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = title,
                    label = { Text("Title") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    contentPadding = it,
                )
            }
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = description,
                    label = { Text("Description") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    contentPadding = it,
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Total duration")
                    CountdownDisplay(
                        duration = totalDuration,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}