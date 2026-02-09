package dev.sethdegay.routines.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.core.designsystem.component.CountdownDisplay
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIcon
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val subtleInOutEasing = CubicBezierEasing(0.2f, 0.0f, 0.8f, 1.0f)
private const val hiddenItemAnimationDuration = 70
private const val containerAnimationDuration = 48

private fun <T> subtleInOutTweenSpec(durationMillis: Int) = tween<T>(
    durationMillis = durationMillis,
    easing = subtleInOutEasing,
)

private val horizontalEnterTransition =
    fadeIn(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration)) +
            expandHorizontally(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration))
private val horizontalExitTransition =
    fadeOut(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration)) +
            shrinkHorizontally(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration))

private val verticalEnterTransition =
    fadeIn(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration)) +
            expandVertically(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration))
private val verticalExitTransition =
    fadeOut(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration)) +
            shrinkVertically(animationSpec = subtleInOutTweenSpec(hiddenItemAnimationDuration))

@Composable
fun AccordionHeader(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onClick: (Boolean) -> Unit,
    onLongClick: () -> Unit,
    onPlayButtonClick: () -> Unit,
    title: String,
    description: String,
    totalDuration: Duration,
    padding: PaddingValues,
) {
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = { onClick(!isExpanded) },
                onLongClick = onLongClick,
            )
            .padding(padding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val size = ButtonDefaults.MediumContainerHeight
            AnimatedVisibility(
                visible = isExpanded,
                enter = horizontalEnterTransition,
                exit = horizontalExitTransition,
            ) {
                Button(
                    modifier = Modifier.heightIn(size),
                    shapes = ButtonDefaults.shapes(),
                    onClick = onPlayButtonClick,
                ) {
                    RoutinesIcons.PlayArrow.asComposableIcon(
                        modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)),
                    ).invoke()
                }
            }
            Column(
                modifier = Modifier.animateContentSize(
                    animationSpec = subtleInOutTweenSpec(containerAnimationDuration),
                ),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                AnimatedVisibility(
                    visible = isExpanded && description.isNotBlank(),
                    enter = verticalEnterTransition,
                    exit = verticalExitTransition,
                ) {
                    Text(text = description)
                }
            }
        }
        CountdownDisplay(
            duration = totalDuration,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccordionHeaderPreview() {
    val (isExpanded, setExpanded) = remember { mutableStateOf(true) }
    AccordionHeader(
        modifier = Modifier.fillMaxWidth(),
        isExpanded = isExpanded,
        onClick = { setExpanded(!isExpanded) },
        onLongClick = {},
        title = "Accordion Title",
        description = "Description",
        onPlayButtonClick = {},
        totalDuration = 10.minutes,
        padding = PaddingValues(16.dp),
    )
}