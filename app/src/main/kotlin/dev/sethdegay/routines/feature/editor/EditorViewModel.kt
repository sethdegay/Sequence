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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel(assistedFactory = EditorViewModel.Factory::class)
class EditorViewModel @AssistedInject constructor(
    @Assisted private val id: String?,
    private val routineRepository: RoutineRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: String?): EditorViewModel
    }

    private val _editableUiState = MutableStateFlow<EditorUiState.Success?>(null)

    val uiState: StateFlow<EditorUiState> = _editableUiState
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditorUiState.Loading,
        )

    fun showTaskEditor(task: Task?) {
        _editableUiState.update { it?.copy(showTaskEditorSheet = true, activeTask = task) }
    }

    fun hideTaskEditor() {
        _editableUiState.update { it?.copy(showTaskEditorSheet = false, activeTask = null) }
    }

    fun onTasksSave(tasks: List<Task>) {
        _editableUiState.update {
            it?.copy(
                routine = it.routine.copy(
                    tasks = tasks,
                    dateModified = Clock.System.now(),
                )
            )
        }
    }

    fun onTaskSave(task: Task) {
        _editableUiState.update { state ->
            if (state == null) return@update state
            val updatedTasks = if (state.activeTask == null) {
                state.routine.tasks + task
            } else {
                state.routine.tasks.map { if (state.activeTask.id == task.id) task else it }
            }
            EditorUiState.Success(
                routine = state.routine.copy(
                    tasks = updatedTasks,
                    dateModified = Clock.System.now(),
                ),
                showTaskEditorSheet = false,
                activeTask = null,
            )
        }
    }

    init {
        viewModelScope.launch {
            _editableUiState.debounce(250.milliseconds)
                .filterNotNull()
                .map { it.routine }
                .distinctUntilChanged()
                .collect { routineRepository.saveRoutine(it) }
        }

        if (id != null) {
            viewModelScope.launch {
                _editableUiState.value = EditorUiState.Success(
                    routine = routineRepository.getRoutine(id).first(),
                )
            }
        } else {
            _editableUiState.value = EditorUiState.Success(
                routine = with(Clock.System.now()) {
                    Routine(
                        title = "",
                        description = null,
                        routineType = RoutineType.GENERIC,
                        dateCreated = this,
                        dateModified = this,
                        tasks = emptyList(),
                    )
                },
            )
        }
    }
}