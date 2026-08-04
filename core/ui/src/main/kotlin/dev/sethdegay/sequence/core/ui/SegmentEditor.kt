package dev.sethdegay.sequence.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.CardGroup
import dev.sethdegay.sequence.core.designsystem.component.ExpressiveButton
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.SegmentInputMethod
import dev.sethdegay.sequence.core.ui.R.string
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun SegmentEditor(
    segment: Segment,
    onSegmentUpdate: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    inputMethod: SegmentInputMethod,
    onInputMethodChange: (SegmentInputMethod) -> Unit,
) {
    val titleState = rememberTextFieldState(segment.title)
    val typeState = rememberTextFieldState(segment.duration.toString())
    var typeTextFieldIsError by remember { mutableStateOf(false) }
    val pickerState = rememberDurationPickerState(segment.duration)
    Column(
        modifier = Modifier
            .padding(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 16.dp,
            )
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CardGroup {
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = titleState,
                    label = { Text(stringResource(string.segment_editor_title_label)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    contentPadding = it,
                )
            }
        }
        DurationInputSwitcher(
            inputMethod = inputMethod,
            onInputMethodChange = onInputMethodChange,
            pickerState = pickerState,
            typeState = typeState,
            durationTextFieldIsError = typeTextFieldIsError,
        )
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            fun construct(): Segment? {
                val newTitle = titleState.text.toString()
                return when (inputMethod) {
                    SegmentInputMethod.PICK -> segment.copy(
                        title = newTitle,
                        duration = pickerState.toDuration()
                    )

                    SegmentInputMethod.TYPE -> {
                        val newDuration = typeState.parseDuration()
                        if (newDuration == null) {
                            typeTextFieldIsError = true
                            null
                        } else {
                            typeTextFieldIsError = false
                            segment.copy(
                                title = newTitle,
                                duration = newDuration
                            )
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .heightIn(ButtonDefaults.MediumContainerHeight)
                    .weight(1f),
                shapes = ButtonDefaults.shapes(),
                onClick = { construct()?.let { onSegmentDelete(it) } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MediumContainerHeight)),
                    imageVector = SequenceIcons.Delete,
                    contentDescription = null,
                )
            }
            ExpressiveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                onClick = { construct()?.let { onSegmentUpdate(it) } },
                size = ButtonDefaults.MediumContainerHeight,
            ) {
                Text(
                    text = stringResource(android.R.string.ok),
                    style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight),
                )
            }
        }
    }
}

@Preview(showBackground = true)
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
    val (inputMethod, onInputMethodChange) =
        remember { mutableStateOf(SegmentInputMethod.PICK) }

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
            onSegmentDelete = {},
            inputMethod = inputMethod,
            onInputMethodChange = onInputMethodChange,
        )
    }
}