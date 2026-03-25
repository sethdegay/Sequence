package dev.sethdegay.sequence.feature.editor_segment.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.sethdegay.sequence.core.designsystem.component.LoadingSection
import dev.sethdegay.sequence.core.ui.SegmentEditor

@Composable
fun SegmentEditorContainer(viewModel: SegmentEditorViewModel, navigateUp: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    uiState.segment.let { segment ->
        when {
            segment != null -> SegmentEditor(
                segment = segment,
                onSegmentUpdate = viewModel::saveSegment,
            )

            else -> LoadingSection()
        }
    }
    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect {
            when (it) {
                is SegmentEditorEffect.Finished -> navigateUp()
            }
        }
    }
}