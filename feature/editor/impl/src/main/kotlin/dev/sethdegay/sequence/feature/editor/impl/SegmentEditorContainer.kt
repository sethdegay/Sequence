package dev.sethdegay.sequence.feature.editor.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.sethdegay.sequence.core.designsystem.component.LoadingSection
import dev.sethdegay.sequence.core.ui.SegmentEditor

@Composable
fun SegmentEditorContainer(viewModel: SegmentEditorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    when {
        uiState.showLoadingIndicator() || uiState.segment == null -> LoadingSection()
        else -> SegmentEditor(
            segment = uiState.segment!!,
            onSegmentUpdate = viewModel::onSegmentUpdate,
        )
    }
}