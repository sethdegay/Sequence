package dev.sethdegay.sequence.feature.sequence.editor.impl

import androidx.compose.foundation.text.input.TextFieldState
import dev.sethdegay.sequence.core.model.Segment
import kotlin.time.Duration

sealed interface SequenceEditorUiState {
    data object Loading : SequenceEditorUiState

    data class Success(
        override val title: TextFieldState,
        override val description: TextFieldState,
        override val segments: List<Segment>,
        override val totalDuration: Duration,
        override val isCreateMode: Boolean?,
    ) : SequenceEditorUiState

    val title: TextFieldState? get() = null

    val description: TextFieldState? get() = null

    val segments: List<Segment>? get() = null

    val totalDuration: Duration? get() = null

    val isCreateMode: Boolean? get() = null
}