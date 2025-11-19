package dev.sethdegay.routines.core.designsystem.util

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
    contentDescription: String? = null,
    enableToolTip: Boolean = true,
): @Composable () -> Unit = {
    if (enableToolTip && contentDescription != null) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = TooltipAnchorPosition.Below,
            ),
            tooltip = {
                PlainTooltip { Text(text = contentDescription) }
            },
            state = rememberTooltipState(),
        ) {
            IconButton(
                modifier = modifier,
                onClick = onClick,
            ) {
                this.asComposableIcon(contentDescription = contentDescription).invoke()
            }
        }
    } else {
        IconButton(
            modifier = modifier,
            onClick = onClick,
        ) {
            this.asComposableIcon(contentDescription = contentDescription).invoke()
        }
    }
}