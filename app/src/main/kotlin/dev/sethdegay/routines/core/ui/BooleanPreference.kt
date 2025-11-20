package dev.sethdegay.routines.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.sethdegay.routines.R.string

@Composable
fun BooleanPreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: @Composable (() -> Unit)? = null,
) {
    Column(modifier = Modifier.clickable { onCheckedChange(!checked) }) {
        ListItem(
            modifier = modifier,
            headlineContent = { Text(title) },
            supportingContent = description?.let { { Text(it) } },
            leadingContent = icon,
            trailingContent = { Switch(checked = checked, onCheckedChange = onCheckedChange) },
            colors = ListItemDefaults.colors(
                containerColor = CardDefaults.cardColors().containerColor,
                headlineColor = CardDefaults.cardColors().contentColor,
                supportingColor = CardDefaults.cardColors().contentColor,
            ),
        )
    }
}

@Composable
fun DynamicColorPreference(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    BooleanPreference(
        modifier = modifier,
        title = stringResource(string.settings_dynamic_color_title),
        description = stringResource(string.settings_dynamic_color_description),
        checked = checked,
        onCheckedChange = onCheckedChange,
        icon = null,
    )
}