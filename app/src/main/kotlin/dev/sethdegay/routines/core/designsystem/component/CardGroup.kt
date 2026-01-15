package dev.sethdegay.routines.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private val paddingDp = 16.dp

private val contentPadding = PaddingValues(0.dp)

private val leadingItemPadding = PaddingValues(
    top = 0.dp,
    start = paddingDp,
    end = paddingDp,
    bottom = 0.dp,
)

private val middleItemPadding = PaddingValues(
    top = 0.dp,
    start = paddingDp,
    end = paddingDp,
    bottom = 0.dp,
)

private val trailingItemPadding = PaddingValues(
    top = 0.dp,
    start = paddingDp,
    end = paddingDp,
    bottom = 0.dp,
)

@Composable
internal fun leadingItemShape(shape: CornerBasedShape = RoundedCornerShape(16.dp)) =
    RoundedCornerShape(
        bottomEnd = middleItemShape().bottomEnd,
        bottomStart = middleItemShape().bottomStart,
        topStart = shape.topStart,
        topEnd = shape.topEnd,
    )

@Composable
internal fun middleItemShape() = RoundedCornerShape(4.dp)

@Composable
internal fun trailingItemShape(shape: CornerBasedShape = RoundedCornerShape(16.dp)) =
    RoundedCornerShape(
        bottomEnd = shape.bottomEnd,
        bottomStart = shape.bottomStart,
        topStart = middleItemShape().topStart,
        topEnd = middleItemShape().topEnd,
    )

@Composable
fun CardGroupLeadingItem(
    shape: Shape = leadingItemShape(),
    content: @Composable (PaddingValues) -> Unit,
) {
    Card(
        modifier = Modifier.padding(leadingItemPadding),
        shape = shape,
    ) {
        content(contentPadding)
    }
}

@Composable
fun CardGroupMiddleItem(content: @Composable (PaddingValues) -> Unit) {
    Card(
        modifier = Modifier.padding(middleItemPadding),
        shape = middleItemShape(),
    ) {
        content(contentPadding)
    }
}

@Composable
fun CardGroupTrailingItem(content: @Composable (PaddingValues) -> Unit) {
    Card(
        modifier = Modifier.padding(trailingItemPadding),
        shape = trailingItemShape(),
    ) {
        content(contentPadding)
    }
}

@Composable
fun CardGroupItem(content: @Composable (PaddingValues) -> Unit) {
    Card(shape = middleItemShape()) {
        content(contentPadding)
    }
}

@Composable
fun CardGroup(content: CardGroupScope.() -> Unit) {
    val scope = remember(content) { CardGroupScope().apply(content) }
    Column(
        verticalArrangement = Arrangement.spacedBy(1.5.dp),
    ) {
        scope.items.forEachIndexed { i, item ->
            when (i) {
                0 -> CardGroupLeadingItem(content = item)
                scope.items.lastIndex -> CardGroupTrailingItem(item)
                else -> CardGroupMiddleItem(item)
            }
        }
    }
}

class CardGroupScope {
    private val _items = mutableListOf<@Composable (PaddingValues) -> Unit>()
    val items: List<@Composable (PaddingValues) -> Unit> = _items

    fun item(content: @Composable (PaddingValues) -> Unit) {
        _items.add(content)
    }
}