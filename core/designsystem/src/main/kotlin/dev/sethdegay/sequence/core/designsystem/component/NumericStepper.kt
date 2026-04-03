package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sethdegay.sequence.core.designsystem.R.string
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.IconButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NumericStepper(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    fontSize: TextUnit = 24.sp,
    minValue: Int = 0,
    maxValue: Int = Int.MAX_VALUE,
    contentPadding: Dp = 8.dp,
    spacing: Dp = 12.dp,
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        NumericStepperButton(
            icon = SequenceIcons.KeyboardArrowUp,
            contentDescription = stringResource(string.increment_content_description),
            enabled = value < maxValue,
        ) {
            val value = value + 1
            if (value <= maxValue) {
                onValueChange(value)
            }
        }
        BasicTextField(
            value = value.toString().padStart(2, '0'),
            onValueChange = {
                val newValue = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
                onValueChange(newValue)
            },
            readOnly = true,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = fontSize,
            ),
        )
        NumericStepperButton(
            icon = SequenceIcons.KeyboardArrowDown,
            contentDescription = stringResource(string.decrement_content_description),
            enabled = value > minValue,
        ) {
            val value = value - 1
            if (value >= minValue) {
                onValueChange(value)
            }
        }
    }
}

@Composable
private fun NumericStepperButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    icon.IconButton(
        modifier = Modifier.autoRepeatClick(enabled = enabled, onClick = onClick),
        onClick = {},
        enabled = enabled,
        contentDescription = contentDescription,
        showTooltip = false,
    )
}

// TODO add haptic feedback
private fun Modifier.autoRepeatClick(
    initialDelay: Duration = 600.milliseconds,
    tickDelay: Duration = 100.milliseconds,
    enabled: Boolean,
    onClick: () -> Unit, // keep onClick light (no network or db)
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val currentOnClick by rememberUpdatedState(onClick)
    this.pointerInput(enabled) {
        if (!enabled) return@pointerInput
        awaitPointerEventScope {
            while (true) {
                awaitFirstDown(requireUnconsumed = false)
                currentOnClick()

                val tickerJob = scope.launch {
                    delay(initialDelay)
                    while (isActive) {
                        currentOnClick()
                        delay(tickDelay)
                    }
                }

                try {
                    waitForUpOrCancellation()
                } finally {
                    tickerJob.cancel()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NumericStepperPreview() {
    val (value, onValueChange) = remember { mutableIntStateOf(0) }
    NumericStepper(
        value = value,
        onValueChange = onValueChange,
        maxValue = 10,
    )
}