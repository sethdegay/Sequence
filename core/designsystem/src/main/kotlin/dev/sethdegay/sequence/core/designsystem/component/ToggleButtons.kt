package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import dev.sethdegay.sequence.core.designsystem.util.asComposableIcon

enum class ToggleButtonPosition { LEADING, MIDDLE, TRAILING; }

data class ToggleButtonOption<T>(
    val label: String,
    val iconChecked: ImageVector,
    val iconUnchecked: ImageVector,
    val value: T,
    val onValueChanged: (T) -> Unit,
    val toggleButtonPosition: ToggleButtonPosition,
)

@Composable
fun <T> ToggleButtons(
    modifier: Modifier = Modifier,
    options: List<ToggleButtonOption<T>>,
    onCheckedRequest: (T) -> Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        options.forEach { toggleButtonOption ->
            ToggleButton(
                modifier = Modifier.weight(1f),
                toggleButtonOption = toggleButtonOption,
                checked = onCheckedRequest(toggleButtonOption.value),
            )
        }
    }
}

@Composable
fun <T> ToggleButton(
    modifier: Modifier = Modifier,
    toggleButtonOption: ToggleButtonOption<T>,
    checked: Boolean,
    enabled: Boolean = true,
) {
    ToggleButton(
        checked = checked,
        onCheckedChange = { toggleButtonOption.onValueChanged(toggleButtonOption.value) },
        modifier = modifier
            .semantics { role = Role.RadioButton }
            .fillMaxWidth(),
        shapes = when (toggleButtonOption.toggleButtonPosition) {
            ToggleButtonPosition.LEADING -> ButtonGroupDefaults.connectedLeadingButtonShapes()
            ToggleButtonPosition.TRAILING -> ButtonGroupDefaults.connectedTrailingButtonShapes()
            ToggleButtonPosition.MIDDLE -> ButtonGroupDefaults.connectedMiddleButtonShapes()
        },
        enabled = enabled,
    ) {
        (if (checked) toggleButtonOption.iconChecked else toggleButtonOption.iconUnchecked)
            .asComposableIcon().invoke()
        Spacer(modifier = Modifier.size(ToggleButtonDefaults.IconSpacing))
        Text(toggleButtonOption.label)
    }
}
