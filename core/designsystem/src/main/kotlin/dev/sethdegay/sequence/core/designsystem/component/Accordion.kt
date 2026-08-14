package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val ENTER_ANIMATION_DURATION = 150
private const val EXIT_ANIMATION_DURATION = 150
private val SPACING = ButtonGroupDefaults.ConnectedSpaceBetween

@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    header: @Composable (PaddingValues) -> Unit,
    content: AccordionScope.() -> Unit,
) {
    val scope = remember(content) { AccordionScope().apply(content) }

    val headerBottomCornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 4.dp else 16.dp,
        animationSpec = tween(if (isExpanded) ENTER_ANIMATION_DURATION else EXIT_ANIMATION_DURATION),
        label = "Header Corner Animation"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SPACING),
    ) {
        CardGroupLeadingItem(
            shape = leadingItemShape().copy(
                bottomStart = CornerSize(headerBottomCornerRadius),
                bottomEnd = CornerSize(headerBottomCornerRadius)
            ),
            content = header,
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = tween(ENTER_ANIMATION_DURATION)
            ) + fadeIn(
                animationSpec = tween(ENTER_ANIMATION_DURATION)
            ),
            exit = shrinkVertically(
                animationSpec = tween(EXIT_ANIMATION_DURATION)
            ) + fadeOut(
                animationSpec = tween(EXIT_ANIMATION_DURATION)
            ),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(SPACING),
            ) {
                scope.items.forEachIndexed { i, item ->
                    if (i == scope.items.lastIndex) {
                        CardGroupTrailingItem(item)
                    } else {
                        CardGroupMiddleItem(item)
                    }
                }
            }
        }
    }
}

class AccordionScope {
    private val _items = mutableListOf<@Composable (PaddingValues) -> Unit>()
    val items: List<@Composable (PaddingValues) -> Unit> = _items

    fun item(content: @Composable (PaddingValues) -> Unit) {
        _items.add(content)
    }
}

@Preview(showBackground = true)
@Composable
private fun AccordionPreview() {
    val (isExpanded, setExpanded) = remember { mutableStateOf(true) }

    Accordion(
        isExpanded = isExpanded,
        header = {
            Text(
                modifier = Modifier
                    .clickable { setExpanded(!isExpanded) }
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Accordion Title",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    ) {
        fun itemText(text: String) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = text
                )
            }
        }

        itemText("Task A")
        itemText("Task B")
        itemText("Task C")
    }
}