package dev.sethdegay.sequence.core.designsystem.util

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
private fun ConditionalTooltipWrapper(
    showTooltip: Boolean,
    tooltip: @Composable TooltipScope.() -> Unit,
    position: TooltipAnchorPosition = TooltipAnchorPosition.Below,
    content: @Composable () -> Unit,
) {
    if (showTooltip) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = position,
            ),
            tooltip = tooltip,
            state = rememberTooltipState(),
            content = content,
        )
    } else {
        content()
    }
}

@Composable
fun ImageVector.Icon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        modifier = modifier,
        imageVector = this,
        contentDescription = contentDescription,
        tint = tint,
    )
}

@Composable
fun ImageVector.IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    contentDescription: String? = null,
    showTooltip: Boolean = true,
    tooltipText: String? = null,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Below,
    tint: Color = LocalContentColor.current,
) {
    val effectiveTooltipText = tooltipText ?: contentDescription
    ConditionalTooltipWrapper(
        showTooltip = showTooltip && effectiveTooltipText != null,
        position = tooltipPosition,
        tooltip = {
            effectiveTooltipText?.let {
                PlainTooltip { Text(text = it) }
            }
        },
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
        ) {
            this.Icon(
                contentDescription = contentDescription,
                tint = tint,
            )
        }
    }
}