package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.R.string

@Composable
fun DotSeparatedContent(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    color: Color = Color.Unspecified,
    space: Dp = 8.dp,
    content: DotSeparatedContentScope.() -> Unit,
) {
    val scope = remember(content) { DotSeparatedContentScope().apply(content) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
    ) {
        scope.items.forEachIndexed { i, item ->
            item()
            if (i < scope.items.lastIndex) {
                Separator(style, color)
            }
        }
    }
}

class DotSeparatedContentScope {
    internal val items = mutableListOf<@Composable () -> Unit>()

    fun item(content: @Composable () -> Unit) {
        items.add(content)
    }
}

@Composable
private fun Separator(
    style: TextStyle = MaterialTheme.typography.bodySmall,
    color: Color = Color.Unspecified,
) {
    Text(
        text = stringResource(string.dot_separator),
        style = style,
        color = color,
    )
}

@Preview(showBackground = true)
@Composable
private fun DotSeparatedContentPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            DotSeparatedContent(
                space = 8.dp,
                style = MaterialTheme.typography.bodySmall,
            ) {
                item {
                    Text(text = "5 min read")
                }
                item {
                    Text(
                        text = "Android",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                item {
                    Text(text = "Aug 26, 2026")
                }
            }
        }
    }
}