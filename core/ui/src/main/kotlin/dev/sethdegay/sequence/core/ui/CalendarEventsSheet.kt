package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        when {
            calendarEvents == null -> {
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
            }

            calendarEvents.isEmpty() -> {
                onDismissRequest()
            }

            else -> CalendarEventList(calendarEvents)
        }
    }
}

@Composable
private fun CalendarEventList(events: List<CalendarEvent>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        itemsIndexed(events) { index, event ->
            val isFirst = index == 0
            val isLast = index == events.lastIndex
            TimelineItem(
                event = event,
                showTopLine = !isFirst,
                showBottomLine = !isLast,
            )
        }
    }
}

@Composable
private fun CalendarEventRow(event: CalendarEvent) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = event.sequence.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "${event.start.toShortTimeString()} - ${event.end.toShortTimeString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text("•", style = MaterialTheme.typography.bodySmall)
            DurationDisplay(
                duration = event.duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun TimelineItem(
    event: CalendarEvent,
    showTopLine: Boolean,
    showBottomLine: Boolean
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(),
        ) {
            TimelineConnector(modifier = Modifier.weight(1f), isVisible = showTopLine)
            Box(
                Modifier
                    .size(10.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
            TimelineConnector(modifier = Modifier.weight(1f), isVisible = showBottomLine)
        }
        CalendarEventRow(event)
    }
}

@Composable
private fun TimelineConnector(modifier: Modifier = Modifier, isVisible: Boolean) {
    Spacer(
        modifier
            .width(2.dp)
            .background(if (isVisible) MaterialTheme.colorScheme.outlineVariant else Color.Transparent),
    )
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
    )
}
