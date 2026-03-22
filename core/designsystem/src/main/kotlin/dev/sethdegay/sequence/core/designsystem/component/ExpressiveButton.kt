package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExpressiveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    size: Dp = ButtonDefaults.LargeContainerHeight,
    content: @Composable (RowScope.() -> Unit),
) {
    Button(
        modifier = modifier.heightIn(size),
        onClick = onClick,
        contentPadding = ButtonDefaults.contentPaddingFor(size),
        shapes = ButtonDefaults.shapes(),
        colors = colors,
        content = content,
    )
}

@Composable
fun ExpressiveOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    size: Dp = ButtonDefaults.MediumContainerHeight,
    content: @Composable (RowScope.() -> Unit),
) {
    OutlinedButton(
        modifier = modifier.heightIn(size),
        onClick = onClick,
        enabled = enabled,
        contentPadding = ButtonDefaults.contentPaddingFor(size),
        shapes = ButtonDefaults.shapes(),
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpressiveButtonsPreview() {
    val mediumButtonSize = ButtonDefaults.MediumContainerHeight
    val largeButtonSize = ButtonDefaults.LargeContainerHeight
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ExpressiveButton(
            onClick = {},
            size = largeButtonSize
        ) {
            Text(
                text = "Button",
                style = ButtonDefaults.textStyleFor(largeButtonSize),
            )
        }
        ExpressiveOutlinedButton(
            onClick = {},
            size = mediumButtonSize
        ) {
            Text(
                text = "Outlined Button",
                style = ButtonDefaults.textStyleFor(mediumButtonSize),
            )
        }
    }
}