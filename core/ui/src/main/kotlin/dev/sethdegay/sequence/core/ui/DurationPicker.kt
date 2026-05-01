package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.NumericStepper
import dev.sethdegay.sequence.core.designsystem.component.NumericStepperOrientation
import dev.sethdegay.sequence.core.ui.R.string
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun DurationPicker(modifier: Modifier = Modifier, state: DurationPickerState) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LabeledNumericStepper(
            value = state.hours,
            onValueChange = { state.hours = it },
            label = stringResource(string.duration_picker_hours_label),
            minValue = 0,
            maxValue = 24,
        )
        LabeledNumericStepper(
            value = state.minutes,
            onValueChange = { state.minutes = it },
            label = stringResource(string.duration_picker_minutes_label),
            minValue = 0,
            maxValue = 60,
        )
        LabeledNumericStepper(
            value = state.seconds,
            onValueChange = { state.seconds = it },
            label = stringResource(string.duration_picker_seconds_label),
            minValue = 0,
            maxValue = 60,
        )
    }
}

@Composable
private fun LabeledNumericStepper(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    minValue: Int,
    maxValue: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = label)
        NumericStepper(
            value = value,
            onValueChange = onValueChange,
            minValue = minValue,
            maxValue = maxValue,
            contentPadding = 0.dp,
            orientation = NumericStepperOrientation.VERTICAL,
        )
    }
}

class DurationPickerState(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    initialSeconds: Int = 0,
) {
    private var _hours by mutableIntStateOf(value = initialHours.coerceIn(0, 24))
    var hours: Int
        get() = _hours
        set(value) {
            _hours = value.coerceIn(0, 24)
        }

    private var _minutes by mutableIntStateOf(value = initialMinutes.coerceIn(0, 60))
    var minutes: Int
        get() = _minutes
        set(value) {
            _minutes = value.coerceIn(0, 60)
        }

    private var _seconds by mutableIntStateOf(value = initialSeconds.coerceIn(0, 60))
    var seconds: Int
        get() = _seconds
        set(value) {
            _seconds = value.coerceIn(0, 60)
        }

    fun toDuration(): Duration = hours.hours + minutes.minutes + seconds.seconds
}

@Composable
fun rememberDurationPickerState(initialDuration: Duration = Duration.ZERO): DurationPickerState {
    return initialDuration.toComponents { hours, minutes, seconds, _ ->
        remember {
            DurationPickerState(
                initialHours = hours.toInt(),
                initialMinutes = minutes,
                initialSeconds = seconds,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DurationPickerPickerPreview() {
    val state = rememberDurationPickerState()
    DurationPicker(state = state)
}