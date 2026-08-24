package dev.sethdegay.sequence.feature.calendarlogs.impl

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

@HiltViewModel(assistedFactory = CalendarLogsViewModel.Factory::class)
class CalendarLogsViewModel @AssistedInject constructor(
    @Assisted range: ClosedRange<Instant>,
    calendarEventRepository: CalendarEventRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(range: ClosedRange<Instant>): CalendarLogsViewModel
    }

    val uiState: StateFlow<CalendarLogsUiState> =
        calendarEventRepository.getCalendarEvents(start = range.start, end = range.endInclusive)
            .map { CalendarLogsUiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CalendarLogsUiState.Loading,
            )
}