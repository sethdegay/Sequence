package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.ToggleGroup
import dev.sethdegay.sequence.core.designsystem.component.ToggleGroupScope

@Composable
fun <T> TogglePreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    onCheckedRequest: (T) -> Boolean,
    content: ToggleGroupScope<T>.() -> Unit,
) {
    Column(modifier = modifier) {
        ListItem(
            content = { Text(title) },
            supportingContent = description?.let { { Text(it) } },
            colors = ListItemDefaults.colors(
                containerColor = CardDefaults.cardColors().containerColor,
                headlineColor = CardDefaults.cardColors().contentColor,
                supportingColor = CardDefaults.cardColors().contentColor,
            ),
        )
        ToggleGroup(
            modifier = Modifier.padding(
                top = 0.dp,
                start = 12.dp,
                bottom = 12.dp,
                end = 12.dp
            ),
            onCheckedRequest = onCheckedRequest,
            content = content,
        )
    }
}
