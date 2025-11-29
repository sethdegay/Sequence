package dev.sethdegay.routines.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
private fun leadingItemShape(shape: CornerBasedShape = MaterialTheme.shapes.large) =
    RoundedCornerShape(
        bottomEnd = middleItemShape().bottomEnd,
        bottomStart = middleItemShape().bottomStart,
        topStart = shape.topStart,
        topEnd = shape.topEnd,
    )

@Composable
private fun middleItemShape() = MaterialTheme.shapes.extraSmall

@Composable
private fun trailingItemShape(shape: CornerBasedShape = MaterialTheme.shapes.large) =
    RoundedCornerShape(
        bottomEnd = shape.bottomEnd,
        bottomStart = shape.bottomStart,
        topStart = middleItemShape().topStart,
        topEnd = middleItemShape().topEnd,
    )

@Composable
fun CardGroupLeadingItem(content: @Composable (PaddingValues) -> Unit) {
    Card(
        modifier = Modifier.padding(leadingItemPadding),
        shape = leadingItemShape(),
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
fun CardGroup(vararg items: @Composable (PaddingValues) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.5.dp),
    ) {
        items.forEachIndexed { i, item ->
            when (i) {
                0 -> CardGroupLeadingItem(item)
                items.lastIndex -> CardGroupTrailingItem(item)
                else -> CardGroupMiddleItem(item)
            }
        }
    }
}