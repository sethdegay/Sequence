package dev.sethdegay.sequence.feature.home.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.sethdegay.sequence.core.designsystem.component.LoadingSection
import dev.sethdegay.sequence.core.ui.CalendarEventList

@Composable
fun EventSheetContainer(viewModel: EventSheetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    when {
        uiState.showLoadingIndicator() || uiState.events.isEmpty() -> LoadingSection()
        else -> CalendarEventList(uiState.events)
    }
}