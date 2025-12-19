package dev.sethdegay.routines.feature.home

import dev.sethdegay.routines.core.model.Routine

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        override val routines: List<Routine>,
    ) : HomeUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val routines: List<Routine> get() = emptyList()
}