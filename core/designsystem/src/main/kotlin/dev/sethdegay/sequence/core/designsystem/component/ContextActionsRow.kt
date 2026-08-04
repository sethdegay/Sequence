package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.theme.SequenceTheme

data class ContextAction(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val contentDescription: String? = null,
    val isDangerous: Boolean = false,
)

@Composable
fun ContextActionsRow(
    actions: List<ContextAction>,
    modifier: Modifier = Modifier,
) {
    val size = ButtonDefaults.MediumContainerHeight
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(actions) { action ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    modifier = Modifier.heightIn(size),
                    shapes = ButtonDefaults.shapes(),
                    onClick = action.onClick,
                    colors = if (action.isDangerous) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)),
                        imageVector = action.icon,
                        contentDescription = action.contentDescription ?: action.label,
                    )
                }
                Text(
                    text = action.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContextActionsRowPreview() {
    val actions = listOf(
        ContextAction(
            label = "Add",
            icon = SequenceIcons.Add,
            onClick = {},
        ),
        ContextAction(
            label = "Play",
            icon = SequenceIcons.PlayArrow,
            onClick = {},
        ),
        ContextAction(
            label = "Pick",
            icon = SequenceIcons.Pick,
            onClick = {},
        ),
        ContextAction(
            label = "Delete",
            icon = SequenceIcons.Delete,
            onClick = {},
            isDangerous = true,
        ),
    )
    SequenceTheme {
        ContextActionsRow(actions)
    }
}