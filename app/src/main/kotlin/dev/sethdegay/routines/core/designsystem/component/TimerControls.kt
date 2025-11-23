package dev.sethdegay.routines.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.sethdegay.routines.R
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIcon

enum class TimerControlsMode { RUNNING, PAUSED; }

interface TimerControlsActions {
    fun onToggleTimer()
    fun onPrevious()
    fun onNext()
}

@Composable
fun TimerControls(
    mode: TimerControlsMode,
    startText: String,
    pauseText: String,
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
        OutlinedButton(
            modifier = Modifier
                .heightIn(mediumButtonSize)
                .weight(1f),
            onClick = actions::onPrevious,
            enabled = canMovePrevious,
            contentPadding = ButtonDefaults.contentPaddingFor(mediumButtonSize),
            shapes = ButtonDefaults.shapes(),
        ) {
            RoutinesIcons.Previous.asComposableIcon(
                modifier = Modifier.size(ButtonDefaults.iconSizeFor(largeButtonSize))
            ).invoke()
        }
        Button(
            modifier = Modifier
                .heightIn(largeButtonSize)
                .weight(2f),
            onClick = actions::onToggleTimer,
            contentPadding = ButtonDefaults.contentPaddingFor(largeButtonSize),
            shapes = ButtonDefaults.shapes(),
            colors =
                when (mode) {
                    TimerControlsMode.RUNNING -> ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )

                    TimerControlsMode.PAUSED -> ButtonDefaults.buttonColors()
                }
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
        OutlinedButton(
            modifier = Modifier
                .heightIn(mediumButtonSize)
                .weight(1f),
            onClick = actions::onNext,
            enabled = canMoveNext,
            contentPadding = ButtonDefaults.contentPaddingFor(mediumButtonSize),
            shapes = ButtonDefaults.shapes(),
        ) {
            RoutinesIcons.Next.asComposableIcon(
                modifier = Modifier.size(ButtonDefaults.iconSizeFor(largeButtonSize))
            ).invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimerControlsPreview() {
    var mode by remember { mutableStateOf(TimerControlsMode.PAUSED) }
    TimerControls(
        mode = mode,
        startText = stringResource(R.string.timer_start_button_text),
        pauseText = stringResource(R.string.timer_pause_button_text),
        actions = object : TimerControlsActions {
            override fun onToggleTimer() {
                mode = if (mode == TimerControlsMode.RUNNING) {
                    TimerControlsMode.PAUSED
                } else {
                    TimerControlsMode.RUNNING
                }
            }

            override fun onNext() {}
            override fun onPrevious() {}
        },
        canMovePrevious = false,
        canMoveNext = true,
    )
}
