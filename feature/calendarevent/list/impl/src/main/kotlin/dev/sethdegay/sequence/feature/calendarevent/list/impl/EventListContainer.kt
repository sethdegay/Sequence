package dev.sethdegay.sequence.feature.calendarevent.list.impl

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.LoadingSection
import dev.sethdegay.sequence.core.ui.CalendarEventList

@Composable
fun EventListContainer(viewModel: EventListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    when {
        uiState.showLoadingIndicator() || uiState.events.isEmpty() -> LoadingSection()
        else -> CalendarEventList(
            events = uiState.events,
            contentPadding = PaddingValues(
                top = 0.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
        )
    }
}