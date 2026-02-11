package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons

@Composable
fun BooleanPreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        modifier = modifier
            .clickable(
                enabled = isEnabled,
                onClick = { onCheckedChange(!checked) },
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            )
            .alpha(if (isEnabled) 1f else 0.38f),
        headlineContent = { Text(title) },
        supportingContent = description?.let { { Text(it) } },
        leadingContent = icon,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = isEnabled,
                interactionSource = interactionSource,
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = CardDefaults.cardColors().containerColor,
            headlineColor = CardDefaults.cardColors().contentColor,
            supportingColor = CardDefaults.cardColors().contentColor,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun BooleanPreferencePreview() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }
    var checked3 by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        BooleanPreference(
            title = "Enable Notifications",
            description = "Receive push notifications for new messages",
            checked = checked1,
            onCheckedChange = { checked1 = it },
            icon = { Icon(SequenceIcons.Settings, contentDescription = null) },
        )

        BooleanPreference(
            title = "Dark Mode",
            checked = checked2,
            onCheckedChange = { checked2 = it },
            icon = { Icon(SequenceIcons.Settings, contentDescription = null) },
        )

        BooleanPreference(
            title = "Developer Options",
            description = "This setting is managed by your administrator",
            checked = checked3,
            onCheckedChange = { checked3 = it },
            isEnabled = false,
            icon = { Icon(SequenceIcons.Settings, contentDescription = null) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BooleanPreferenceInteractivePreview() {
    var isEnabled by remember { mutableStateOf(true) }
    var masterSwitch by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        BooleanPreference(
            title = "Master Control",
            description = "Toggle this to enable/disable the setting below",
            checked = masterSwitch,
            onCheckedChange = {
                masterSwitch = it
                isEnabled = it
            },
        )

        BooleanPreference(
            title = "Dependent Setting",
            description = "This is only active when Master Control is on",
            checked = true,
            onCheckedChange = { },
            isEnabled = isEnabled,
        )
    }
}