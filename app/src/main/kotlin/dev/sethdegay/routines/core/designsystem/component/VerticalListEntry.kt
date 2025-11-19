package dev.sethdegay.routines.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalListEntry(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            icon()
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            if (description != null) {
                Text(text = description)
            }
        }
    }
}
