package dev.sethdegay.sequence.core.ui

import android.text.format.DateUtils
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import dev.sethdegay.sequence.core.designsystem.component.DotSeparatedContent
import dev.sethdegay.sequence.core.ui.R.string
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun TimestampSummary(
    dateCreated: Instant,
    dateModified: Instant,
) {
    val created = remember(dateCreated) {
        val javaInstant = dateCreated.toJavaInstant()
        val formatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withZone(ZoneId.systemDefault())
            .withLocale(Locale.getDefault())
        formatter.format(javaInstant)
    }

    val modified = remember(dateModified) {
        DateUtils.getRelativeTimeSpanString(
            dateModified.toEpochMilliseconds(),
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
        ).toString()
    }.let {
        stringResource(string.timestamp_summary_updated, it)
    }

    DotSeparatedContent {
        item {
            Text(
                text = created,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            Text(
                text = modified,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
