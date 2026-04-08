package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.sethdegay.sequence.core.ui.R.string
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private val regex by lazy {
    Regex(
        """(?:(?<days>\d+)\s*d\w*)?\s*(?:(?<hours>\d+)\s*h\w*)?\s*(?:(?<minutes>\d+)\s*m\w*)?\s*(?:(?<seconds>\d+)\s*s\w*)?""",
        RegexOption.IGNORE_CASE,
    )
}

internal fun TextFieldState.parseDuration(): Duration? {
    return regex.find(text.toString())
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
fun DurationTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    isError: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(),
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    ),
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        state = state,
        label = { Text(text = stringResource(string.duration_text_field_label)) },
        isError = isError,
        colors = colors,
        contentPadding = contentPadding,
    )
}