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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.R.string
import dev.sethdegay.sequence.core.designsystem.component.DurationDisplay
import dev.sethdegay.sequence.core.designsystem.theme.SequenceTheme
import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.model.Sequence
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun CalendarEventList(events: List<CalendarEvent>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        itemsIndexed(
            items = events,
            key = { _, event -> event.id },
        ) { index, event ->
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
    val timeRange = remember(event.start, event.end) {
        "${event.start.toShortTimeString()} - ${event.end.toShortTimeString()}"
    }
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
                text = timeRange,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(stringResource(string.dot_separator), style = MaterialTheme.typography.bodySmall)
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
                .width(32.dp)
                .fillMaxHeight(),
        ) {
            if (showTopLine) {
                TimelineConnector(modifier = Modifier.weight(1f))
            } else {
                Spacer(Modifier.weight(1f))
            }

            Box(
                Modifier
                    .size(10.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )

            if (showBottomLine) {
                TimelineConnector(modifier = Modifier.weight(1f))
            } else {
                Spacer(Modifier.weight(1f))
            }
        }
        CalendarEventRow(event)
    }
}

@Composable
private fun TimelineConnector(modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .width(2.dp)
            .background(MaterialTheme.colorScheme.outlineVariant),
    )
}

private fun Instant.toShortTimeString(): String {
    val formatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this.toJavaInstant())
}

@Preview(showBackground = true)
@Composable
private fun CalendarEventListPreview(
    @PreviewParameter(CalendarStateProvider::class) events: List<CalendarEvent>,
) {
    SequenceTheme {
        CalendarEventList(events)
    }
}

private fun createMockEvent(
    title: String,
    startTime: Instant,
    duration: Duration
) = CalendarEvent(
    start = startTime,
    end = startTime.plus(duration),
    duration = duration,
    sequence = Sequence(
        title = title,
        description = "This is a sequence description for $title",
        dateCreated = Clock.System.now(),
        dateModified = Clock.System.now(),
        segments = emptyList(),
        totalDuration = duration,
    )
)

class CalendarStateProvider : PreviewParameterProvider<List<CalendarEvent>?> {
    override val values = sequenceOf(
        emptyList(),
        listOf(createMockEvent("Single Event", Clock.System.now(), 15.minutes)),
        (1..10).map { createMockEvent("Event $it", Clock.System.now(), 10.minutes) },
    )
}