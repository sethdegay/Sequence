package dev.sethdegay.routines.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.data.repository.RoutineRepository
import dev.sethdegay.routines.core.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    routineRepository: RoutineRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        routineRepository.getRoutines(),
        userPreferencesRepository.uiState
    ) { routines, uiState ->
        HomeUiState.Success(
            routines = routines,
            routinesAccordionExpandedId = uiState.routinesAccordionExpandedId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState.Loading,
    )

    fun setRoutinesAccordionExpandedId(routinesAccordionExpandedId: String?) {
        viewModelScope.launch {
            userPreferencesRepository.setRoutinesAccordionExpandedId(routinesAccordionExpandedId)
        }
    }
}