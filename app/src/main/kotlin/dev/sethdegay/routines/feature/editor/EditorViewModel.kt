package dev.sethdegay.routines.feature.editor

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel(assistedFactory = EditorViewModel.Factory::class)
class EditorViewModel @AssistedInject constructor(
    @Assisted private val id: Long?,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long?): EditorViewModel
    }

    private val _idFlow = MutableStateFlow<Long?>(null)
    val idFlow: StateFlow<Long?>
        get() = _idFlow

    init {
        _idFlow.value = id
    }
}