package dev.sethdegay.routines.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.VerticalListEntry

@Composable
fun BooleanPreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VerticalListEntry(
            modifier = Modifier
                .weight(1f)
                .padding(PaddingValues(end = 16.dp)),
            title = title,
            description = description,
            icon = icon,
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
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