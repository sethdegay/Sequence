package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.DurationDisplay
import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.model.Sequence
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun CalendarEventsSheet(
    calendarEvents: List<CalendarEvent>?,
    onDismissRequest: () -> Unit,
    showLoadingIndicator: Boolean = calendarEvents == null,
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        if (showLoadingIndicator) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            ) {
                LoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp),
                )
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                CalendarEventsSheetContent(calendarEvents)
            }
        }
    }
}

@Composable
private fun CalendarEventsSheetContent(calendarEvents: List<CalendarEvent>?) {
    calendarEvents?.forEachIndexed { i, calendarEvent ->
        val start = calendarEvent.start.toShortTimeString()
        val end = calendarEvent.end.toShortTimeString()
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$start - $end",
                    style = MaterialTheme.typography.labelSmall,
                )
                DurationDisplay(
                    duration = calendarEvent.duration,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Text(
                text = calendarEvent.sequence.title,
                style = MaterialTheme.typography.bodyMediumEmphasized,
            )
        }
        if (i != calendarEvents.lastIndex) {
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

private fun Instant.toShortTimeString(): String {
    val formatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this.toJavaInstant())
}

@Preview
@Composable
private fun CalendarEventsSheetPreview() {
    CalendarEventsSheet(
        calendarEvents = listOf(
            CalendarEvent(
                start = Clock.System.now().minus(10.minutes),
                end = Clock.System.now(),
                duration = 10.minutes,
                sequence = Sequence(
                    title = "Sequence A",
                    description = "This is a sequence",
                    dateCreated = Clock.System.now(),
                    dateModified = Clock.System.now(),
                    steps = emptyList(),
                    totalDuration = Duration.ZERO,
                )
            ),
            CalendarEvent(
                start = Clock.System.now().minus(5.minutes + 40.seconds),
                end = Clock.System.now(),
                duration = 5.minutes + 40.seconds,
                sequence = Sequence(
                    title = "Sequence B",
                    description = "This is another sequence",
                    dateCreated = Clock.System.now(),
                    dateModified = Clock.System.now(),
                    steps = emptyList(),
                    totalDuration = Duration.ZERO,
                )
            ),
        ),
        onDismissRequest = { },
        showLoadingIndicator = false,
    )
}