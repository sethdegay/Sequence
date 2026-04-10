package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.IconButton
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
            ListItem(
                headlineContent = { Text(stringResource(string.duration_input_switcher_title)) },
                trailingContent = {
                    when (mode) {
                        DurationInputSwitcherMode.PICK -> SequenceIcons.Keyboard.IconButton(
                            onClick = { onModeChange(DurationInputSwitcherMode.TYPE) },
                            contentDescription = stringResource(string.duration_input_switcher_mode_type),
                        )

                        DurationInputSwitcherMode.TYPE -> SequenceIcons.Pick.IconButton(
                            onClick = { onModeChange(DurationInputSwitcherMode.PICK) },
                            contentDescription = stringResource(string.duration_input_switcher_mode_pick),
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = CardDefaults.cardColors().containerColor,
                    headlineColor = CardDefaults.cardColors().contentColor,
                    supportingColor = CardDefaults.cardColors().contentColor,
                ),
            )
        }
        item {
            when (mode) {
                DurationInputSwitcherMode.PICK -> DurationPicker(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .padding(it),
                    state = durationPickerState,
                )

                DurationInputSwitcherMode.TYPE -> {
                    DurationTextField(
                        state = durationTextFieldState,
                        isError = durationTextFieldIsError,
                        contentPadding = it,
                    )
                }
            }
        }
    }
}