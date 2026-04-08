package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.ui.R.string

enum class DurationInputSwitcherMode { PICK, TYPE; }

// TODO
// ux: make sure picker and TextField are automatically populated when switching
// ux: save preferred input method to ui state pref
@Composable
fun DurationInputSwitcher(
    mode: DurationInputSwitcherMode,
    onModeChange: (DurationInputSwitcherMode) -> Unit,
    durationPickerState: DurationPickerState,
    durationTextFieldState: TextFieldState,
    durationTextFieldIsError: Boolean,
) {
    CardGroup {
        item {
            TogglePreference(
                title = stringResource(string.duration_input_switcher_title),
                description = stringResource(string.duration_input_switcher_description),
                onCheckedRequest = { mode == it }
            ) {
                item(
                    value = DurationInputSwitcherMode.PICK,
                    onValueChanged = onModeChange,
                    label = { stringResource(string.duration_input_switcher_mode_pick) },
                    iconChecked = { SequenceIcons.PickChecked },
                    iconUnchecked = { SequenceIcons.PickUnchecked },
                )
                item(
                    value = DurationInputSwitcherMode.TYPE,
                    onValueChanged = onModeChange,
                    label = { stringResource(string.duration_input_switcher_mode_type) },
                    iconChecked = { SequenceIcons.KeyboardChecked },
                    iconUnchecked = { SequenceIcons.KeyboardUnchecked },
                )
            }
        }
        item { contentPadding ->
            when (mode) {
                DurationInputSwitcherMode.PICK -> DurationPicker(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .padding(contentPadding),
                    state = durationPickerState,
                )

                DurationInputSwitcherMode.TYPE -> {
                    DurationTextField(
                        state = durationTextFieldState,
                        isError = durationTextFieldIsError,
                        contentPadding = contentPadding,
                    )
                }
            }
        }
    }
}