package dev.sethdegay.routines.feature.timer

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.timer.SequentialTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    @Assisted private val id: Long,
    private val timer: SequentialTimer<Task>,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long): TimerViewModel
    }

    private val _idFlow = MutableStateFlow<Long?>(null)
    val idFlow: StateFlow<Long?>
        get() = _idFlow

    init {
        _idFlow.value = id
    }
}