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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import dev.sethdegay.sequence.core.designsystem.util.asComposableIcon

private enum class ToggleGroupItemPosition { LEADING, MIDDLE, TRAILING; }

internal data class ToggleGroupItem<T>(
    val value: T,
    val onValueChanged: (T) -> Unit,
    val label: @Composable () -> String,
    val iconChecked: @Composable () -> ImageVector,
    val iconUnchecked: @Composable () -> ImageVector,
)

class ToggleGroupScope<T> {
    internal val items = mutableListOf<ToggleGroupItem<T>>()

    fun item(
        value: T,
        onValueChanged: (T) -> Unit,
        label: @Composable () -> String,
        iconChecked: @Composable () -> ImageVector,
        iconUnchecked: @Composable () -> ImageVector,
    ) {
        items.add(
            ToggleGroupItem(
                value = value,
                label = label,
                iconChecked = iconChecked,
                iconUnchecked = iconUnchecked,
                onValueChanged = onValueChanged,
            )
        )
    }
}

@Composable
fun <T> ToggleGroup(
    modifier: Modifier = Modifier,
    onCheckedRequest: (T) -> Boolean,
    content: ToggleGroupScope<T>.() -> Unit,
) {
    val scope = remember(content) { ToggleGroupScope<T>().apply(content) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        scope.items.forEachIndexed { index, item ->
            ToggleGroupButton(
                modifier = Modifier.weight(1f),
                item = item,
                checked = onCheckedRequest(item.value),
                position = when {
                    scope.items.size == 1 -> ToggleGroupItemPosition.MIDDLE
                    index == 0 -> ToggleGroupItemPosition.LEADING
                    index == scope.items.lastIndex -> ToggleGroupItemPosition.TRAILING
                    else -> ToggleGroupItemPosition.MIDDLE
                },
            )
        }
    }
}

@Composable
private fun <T> ToggleGroupButton(
    modifier: Modifier = Modifier,
    item: ToggleGroupItem<T>,
    checked: Boolean,
    enabled: Boolean = true,
    position: ToggleGroupItemPosition,
) {
    ToggleButton(
        checked = checked,
        onCheckedChange = { item.onValueChanged(item.value) },
        modifier = modifier
            .semantics { role = Role.RadioButton }
            .fillMaxWidth(),
        shapes = when (position) {
            ToggleGroupItemPosition.LEADING -> ButtonGroupDefaults.connectedLeadingButtonShapes()
            ToggleGroupItemPosition.TRAILING -> ButtonGroupDefaults.connectedTrailingButtonShapes()
            ToggleGroupItemPosition.MIDDLE -> ButtonGroupDefaults.connectedMiddleButtonShapes()
        },
        enabled = enabled,
    ) {
        (if (checked) item.iconChecked else item.iconUnchecked).invoke()
            .asComposableIcon().invoke()
        Spacer(modifier = Modifier.size(ToggleButtonDefaults.IconSpacing))
        Text(item.label())
    }
}
