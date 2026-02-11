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
import dev.sethdegay.sequence.core.designsystem.component.ToggleButtonOption
import dev.sethdegay.sequence.core.designsystem.component.ToggleButtons

@Composable
fun <T> TogglePreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    options: @Composable () -> List<ToggleButtonOption<T>>,
    onCheckedRequest: (T) -> Boolean,
) {
    Column(modifier = modifier) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = description?.let { { Text(it) } },
            colors = ListItemDefaults.colors(
                containerColor = CardDefaults.cardColors().containerColor,
                headlineColor = CardDefaults.cardColors().contentColor,
                supportingColor = CardDefaults.cardColors().contentColor,
            ),
        )
        ToggleButtons(
            modifier = Modifier.padding(
                // See ItemXSpace in: androidx.compose.material3.tokens.ListTokens
                top = 0.dp,
                start = 12.dp,
                bottom = 12.dp,
                end = 12.dp
            ),
            options = options.invoke(),
            onCheckedRequest = onCheckedRequest,
        )
    }
}
