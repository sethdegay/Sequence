package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun DurationDisplay(
    duration: Duration,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Unspecified
) {
    Text(
        text = formatDurationISO(duration),
        modifier = modifier,
        style = style,
        color = color
    )
}

internal fun formatDurationISO(duration: Duration): String {
    return duration.toComponents { days, hours, minutes, seconds, _ ->
        when {
            days > 0 -> "${days}d ${hours}h ${minutes}m ${seconds}s"
            hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }
}

@Composable
fun CountdownDisplay(
    duration: Duration,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.displayMedium,
    color: Color = Color.Unspecified
) {
    val monospaceStyle = style.copy(fontFamily = FontFamily.Monospace)
    Text(
        text = formatDurationDigital(duration),
        modifier = modifier,
        style = monospaceStyle,
        color = color
    )
}

internal fun formatDurationDigital(duration: Duration): String {
    return duration.toComponents { days, hours, minutes, seconds, _ ->
        fun Int.pad(): String = this.toString().padStart(2, '0')
        when {
            days > 0 -> "$days:${hours.pad()}:${minutes.pad()}:${seconds.pad()}"
            hours > 0 -> "${hours.pad()}:${minutes.pad()}:${seconds.pad()}"
            else -> "${minutes.pad()}:${seconds.pad()}"
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DurationDisplayPreview() {
    Column {
        val sampleTime = 3.days + 1.hours + 5.minutes + 9.seconds
        DurationDisplay(
            duration = sampleTime,
            style = MaterialTheme.typography.headlineMedium
        )
        CountdownDisplay(
            duration = sampleTime,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}