package dev.sethdegay.routines.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.core.model.Task
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Stable
private class TaskEditorState(private val initialTask: Task?) {
    val titleState = TextFieldState(initialText = initialTask?.title ?: "")

    var daysInput by mutableStateOf("0")
    var hoursInput by mutableStateOf("0")
    var minutesInput by mutableStateOf("0")
    var secondsInput by mutableStateOf("0")

    init {
        initialTask?.duration?.toComponents { days, hours, minutes, seconds, _ ->
            daysInput = days.toString()
            hoursInput = hours.toString()
            minutesInput = minutes.toString()
            secondsInput = seconds.toString()
        }
    }

    fun toTask(): Task {
        val newDuration = calculateDuration()
        val newTitle = titleState.text.toString()

        return initialTask?.copy(
            title = newTitle,
            duration = newDuration
        ) ?: Task(
            title = newTitle,
            duration = newDuration
        )
    }

    fun isEmpty(): Boolean = initialTask == null &&
            titleState.text.isEmpty() &&
            daysInput == "0" &&
            hoursInput == "0" &&
            minutesInput == "0" &&
            secondsInput == "0"

    private fun calculateDuration(): Duration {
        return daysInput.toLongOrZero().days +
                hoursInput.toLongOrZero().hours +
                minutesInput.toLongOrZero().minutes +
                secondsInput.toLongOrZero().seconds
    }

    private fun String.toLongOrZero(): Long = this.toLongOrNull() ?: 0L
}

private fun handleOnDismissRequest(
    state: TaskEditorState,
    task: Task?,
    onTaskSave: (Task) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (state.isEmpty()) {
        onDismissRequest()
        return
    }
    when (val updatedTask = state.toTask()) {
        task -> onDismissRequest()
        else -> onTaskSave(updatedTask)
    }
}

@Composable
fun TaskEditorSheet(
    task: Task?,
    onTaskSave: (Task) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val state = remember(task) { TaskEditorState(task) }
    ModalBottomSheet(
        onDismissRequest = {
            handleOnDismissRequest(
                state = state,
                task = task,
                onTaskSave = onTaskSave,
                onDismissRequest = onDismissRequest,
            )
        },
    ) {
        TaskEditorSheetContent(state)
    }
}

@Composable
private fun TaskEditorSheetContent(state: TaskEditorState) {
    Column(
        modifier = Modifier.padding(
            start = 16.dp,
            top = 0.dp,
            end = 16.dp,
            bottom = 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = state.titleState,
            label = { Text("Title") },
        )
        DurationInputRow(
            days = state.daysInput,
            onDaysChange = { state.daysInput = it },
            hours = state.hoursInput,
            onHoursChange = { state.hoursInput = it },
            minutes = state.minutesInput,
            onMinutesChange = { state.minutesInput = it },
            seconds = state.secondsInput,
            onSecondsChange = { state.secondsInput = it }
        )
    }
}

@Composable
private fun DurationInputRow(
    days: String,
    onDaysChange: (String) -> Unit,
    hours: String,
    onHoursChange: (String) -> Unit,
    minutes: String,
    onMinutesChange: (String) -> Unit,
    seconds: String,
    onSecondsChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val inputModifier = Modifier.weight(1f)
        TimeInputField(
            modifier = inputModifier,
            label = "Days",
            value = days,
            onValueChange = onDaysChange
        )
        TimeInputField(
            modifier = inputModifier,
            label = "Hours",
            value = hours,
            onValueChange = onHoursChange
        )
        TimeInputField(
            modifier = inputModifier,
            label = "Minutes",
            value = minutes,
            onValueChange = onMinutesChange
        )
        TimeInputField(
            modifier = inputModifier,
            label = "Seconds",
            value = seconds,
            onValueChange = onSecondsChange
        )
    }
}

@Composable
private fun TimeInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(label, maxLines = 1) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

@Preview
@Composable
private fun TaskEditorPreview() {
    TaskEditorSheet(
        task = Task(
            id = 1,
            title = "Task A",
            duration = 30.days + 4.hours + 58.minutes + 15.seconds,
            order = 0,
        ),
        onTaskSave = {},
        onDismissRequest = {},
    )
}