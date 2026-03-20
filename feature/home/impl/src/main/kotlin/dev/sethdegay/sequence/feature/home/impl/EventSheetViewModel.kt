package dev.sethdegay.sequence.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.CalendarEventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Instant

@HiltViewModel(assistedFactory = EventSheetViewModel.Factory::class)
class EventSheetViewModel @AssistedInject constructor(
    @Assisted range: ClosedRange<Instant>,
    calendarEventRepository: CalendarEventRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(range: ClosedRange<Instant>): EventSheetViewModel
    }

    val uiState: StateFlow<EventSheetUiState> =
        calendarEventRepository.getCalendarEvents(start = range.start, end = range.endInclusive)
            .map { EventSheetUiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EventSheetUiState.Loading,
            )
}