package dev.sethdegay.sequence.core.designsystem.util

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
private fun ConditionalTooltipWrapper(
    showTooltip: Boolean,
    tooltip: String?,
    content: @Composable () -> Unit,
) {
    if (showTooltip && tooltip != null) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = TooltipAnchorPosition.Below,
            ),
            tooltip = { PlainTooltip { Text(text = tooltip) } },
            state = rememberTooltipState(),
            content = content,
        )
    } else {
        content()
    }
}

fun ImageVector.asComposableIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
): @Composable () -> Unit = {
    Icon(
        modifier = modifier,
        imageVector = this,
        contentDescription = contentDescription,
    )
}

fun ImageVector.asComposableIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    contentDescription: String? = null,
    showTooltip: Boolean = true,
): @Composable () -> Unit = {
    ConditionalTooltipWrapper(
        showTooltip = showTooltip,
        tooltip = contentDescription,
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
        ) {
            this.asComposableIcon(contentDescription = contentDescription).invoke()
        }
    }
}