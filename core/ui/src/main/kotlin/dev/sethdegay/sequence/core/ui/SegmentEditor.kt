package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.model.Segment
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private val regex = Regex(
    """(?:(?<days>\d+)\s*d\w*)?\s*(?:(?<hours>\d+)\s*h\w*)?\s*(?:(?<minutes>\d+)\s*m\w*)?\s*(?:(?<seconds>\d+)\s*s\w*)?""",
    RegexOption.IGNORE_CASE,
)

fun parseDuration(input: String): Duration? {
    return regex.find(input)
        ?.takeIf { it.value.isNotBlank() }
        ?.let { match ->
            val days = match.groups["days"]?.value?.toInt() ?: 0
            val hours = match.groups["hours"]?.value?.toInt() ?: 0
            val minutes = match.groups["minutes"]?.value?.toInt() ?: 0
            val seconds = match.groups["seconds"]?.value?.toInt() ?: 0
            (days.days + hours.hours + minutes.minutes + seconds.seconds)
                .takeIf { it != Duration.ZERO }
        }
}

@Composable
fun SegmentEditor(
    segment: Segment,
    onSegmentUpdate: (Segment) -> Unit,
) {
    val titleState = rememberTextFieldState(segment.title)
    val durationState = rememberTextFieldState(segment.duration.toString())
    var durationInputIsError by remember { mutableStateOf(false) }
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
            state = titleState,
            label = { Text("Title") },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = durationState,
            label = { Text("Duration") },
            placeholder = { Text("e.g. 10m 30s") },
            isError = durationInputIsError,
        )
        Button(
            onClick = {
                val newTitle = titleState.text.toString()
                val newDuration = parseDuration(durationState.text.toString())
                if (newDuration == null) {
                    durationInputIsError = true
                } else {
                    durationInputIsError = false
                    onSegmentUpdate(segment.copy(title = newTitle, duration = newDuration))
                }
            },
        ) {
            Text(stringResource(android.R.string.ok))
        }
    }
}

@Preview
@Composable
private fun SegmentEditorPreview() {
    val (segment, onSegmentUpdate) = remember {
        mutableStateOf(
            Segment(
                title = "Segment 1",
                duration = 30.days + 4.hours + 58.minutes + 15.seconds,
                order = 0,
            ),
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.tertiary)
                    .padding(16.dp)
                    .fillMaxWidth(),
        ) {
            Text(text = segment.toString(), color = MaterialTheme.colorScheme.onTertiary)
        }
        SegmentEditor(
            segment = segment,
            onSegmentUpdate = onSegmentUpdate,
        )
    }
}