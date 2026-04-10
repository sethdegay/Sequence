package dev.sethdegay.sequence.feature.sequence.editor.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import dev.sethdegay.sequence.core.designsystem.R.string.navigate_up_content_description
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.component.DurationDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.Icon
import dev.sethdegay.sequence.core.designsystem.util.IconButton
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.ui.ReorderableCardGroup
import dev.sethdegay.sequence.feature.segment.editor.api.SegmentEditorNav
import dev.sethdegay.sequence.feature.sequence.editor.impl.R.string

@Composable
fun SequenceEditorScreen(
    viewModel: SequenceEditorViewModel,
    navigateToSegmentEditor: (SegmentEditorNav) -> Unit,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    uiState.totalDuration?.let {
                        DurationDisplay(duration = it, style = LocalTextStyle.current)
                    }
                },
                navigationIcon = {
                    SequenceIcons.NavigateUp.IconButton(
                        onClick = dropUnlessResumed { navigateUp() },
                        contentDescription = stringResource(navigate_up_content_description),
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val key = uiState.segments.let { segments ->
                        SegmentEditorNav.Create(
                            sequenceId = viewModel.getSequenceId(),
                            lastSegmentPosition =
                                if (!segments.isNullOrEmpty()) segments.last().order else 0,
                        )
                    }
                    navigateToSegmentEditor(key)
                },
                content = { SequenceIcons.Add.Icon() },
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
    onSegmentOrderChanged: (List<Segment>) -> Unit,
    onSegmentClick: (Segment) -> Unit,
) {
    ReorderableCardGroup(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .padding(scaffoldPadding)
            .fillMaxSize(),
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
                    label = { Text(stringResource(string.sequence_editor_title_label)) },
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
                    label = { Text(stringResource(string.sequence_editor_description_label)) },
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
        }
    }
}