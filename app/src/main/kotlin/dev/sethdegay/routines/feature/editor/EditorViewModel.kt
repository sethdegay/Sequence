package dev.sethdegay.routines.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.data.repository.RoutineRepository
import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.RoutineType
import dev.sethdegay.routines.core.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel(assistedFactory = EditorViewModel.Factory::class)
class EditorViewModel @AssistedInject constructor(
    @Assisted private val id: Long?,
    private val routineRepository: RoutineRepository,
) : ViewModel() {

    companion object {
        internal val emptyRoutine = with(Clock.System.now()) {
            Routine(
                id = null,
                title = "",
                description = null,
                routineType = RoutineType.GENERIC,
                dateCreated = this,
                dateModified = this,
                tasks = emptyList(),
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Long?): EditorViewModel
    }

    private val _editableRoutine = MutableStateFlow<Routine?>(null)

    val uiState: StateFlow<EditorUiState> = _editableRoutine
        .filterNotNull()
        .map { EditorUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditorUiState.Loading,
        )

    fun setTasks(tasks: List<Task>) {
        _editableRoutine.value = _editableRoutine.value?.copy(tasks = tasks)
    }

    init {
        viewModelScope.launch {
            _editableRoutine.debounce(250.milliseconds)
                .filterNotNull()
                .distinctUntilChanged()
                .collect {
                    val routine = it.copy(
                        dateModified = Clock.System.now(),
                        tasks = it.tasks.mapIndexed { i, task ->
                            task.copy(order = i + 1)
                        }
                    )
                    val id = routineRepository.saveRoutine(routine)
                    _editableRoutine.value = routine.copy(id = id)
                }
        }

        if (id != null) {
            viewModelScope.launch {
                _editableRoutine.value = routineRepository.getRoutine(id).first()
            }
        } else {
            _editableRoutine.value = emptyRoutine
        }
    }
}