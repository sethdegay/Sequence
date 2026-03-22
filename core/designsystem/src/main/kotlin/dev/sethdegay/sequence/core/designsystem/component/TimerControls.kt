package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.sethdegay.sequence.core.designsystem.R.string
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIcon

enum class TimerControlsMode { RUNNING, PAUSED; }

interface TimerControlsActions {
    fun onToggleTimer()
    fun onPrevious()
    fun onNext()
}

@Composable
fun TimerControls(
    mode: TimerControlsMode,
    startText: String = stringResource(string.timer_start_button_text),
    pauseText: String = stringResource(string.timer_pause_button_text),
    actions: TimerControlsActions,
    canMovePrevious: Boolean,
    canMoveNext: Boolean,
) {
    val mediumButtonSize = ButtonDefaults.MediumContainerHeight
    val largeButtonSize = ButtonDefaults.LargeContainerHeight
    Row(
        horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.iconSpacingFor(mediumButtonSize)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExpressiveOutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = actions::onPrevious,
            enabled = canMovePrevious,
            size = mediumButtonSize,
        ) {
            SequenceIcons.Previous.asComposableIcon(
                modifier = Modifier.size(ButtonDefaults.iconSizeFor(largeButtonSize))
            ).invoke()
        }
        ExpressiveButton(
            modifier = Modifier.weight(2f),
            onClick = actions::onToggleTimer,
            colors = when (mode) {
                TimerControlsMode.RUNNING -> ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )

                TimerControlsMode.PAUSED -> ButtonDefaults.buttonColors()
            },
            size = largeButtonSize,
        ) {
            when (mode) {
                TimerControlsMode.RUNNING -> Text(
                    text = pauseText,
                    style = ButtonDefaults.textStyleFor(largeButtonSize),
                )

                TimerControlsMode.PAUSED -> Text(
                    text = startText,
                    style = ButtonDefaults.textStyleFor(largeButtonSize),
                )
            }
        }
        ExpressiveOutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = actions::onNext,
            enabled = canMoveNext,
            size = mediumButtonSize,
        ) {
            SequenceIcons.Next.asComposableIcon(
                modifier = Modifier.size(ButtonDefaults.iconSizeFor(largeButtonSize))
            ).invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimerControlsPreview() {
    val (mode, setMode) = remember { mutableStateOf(TimerControlsMode.PAUSED) }
    TimerControls(
        mode = mode,
        startText = stringResource(string.timer_start_button_text),
        pauseText = stringResource(string.timer_pause_button_text),
        actions = object : TimerControlsActions {
            override fun onToggleTimer() {
                val mode = if (mode == TimerControlsMode.RUNNING) {
                    TimerControlsMode.PAUSED
                } else {
                    TimerControlsMode.RUNNING
                }
                setMode(mode)
            }

            override fun onNext() {}
            override fun onPrevious() {}
        },
        canMovePrevious = false,
        canMoveNext = true,
    )
}
