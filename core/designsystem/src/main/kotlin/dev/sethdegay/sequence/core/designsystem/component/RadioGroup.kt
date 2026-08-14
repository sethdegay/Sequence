package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.theme.SequenceTheme

private val outerCorner = CornerSize(16.dp)
private val innerCorner = CornerSize(4.dp)
private val checkedCorner = CornerSize(50)
private val pressedCorner = CornerSize(4.dp)

private enum class RadioGroupItemPosition {
    SOLO,
    LEADING,
    MIDDLE,
    TRAILING;
}

internal data class RadioGroupItem<T>(
    val value: T,
    val onValueChanged: (T) -> Unit,
    val content: @Composable () -> Unit,
)

class RadioGroupScope<T> {
    internal val items = mutableListOf<RadioGroupItem<T>>()

    fun item(
        value: T,
        onValueChanged: (T) -> Unit,
        content: @Composable () -> Unit,
    ) {
        items.add(
            RadioGroupItem(
                value = value,
                onValueChanged = onValueChanged,
                content = content,
            )
        )
    }
}

private fun calculatePosition(index: Int, totalSize: Int): RadioGroupItemPosition {
    return when {
        totalSize == 1 -> RadioGroupItemPosition.SOLO
        index == 0 -> RadioGroupItemPosition.LEADING
        index == totalSize - 1 -> RadioGroupItemPosition.TRAILING
        else -> RadioGroupItemPosition.MIDDLE
    }
}

@Composable
fun <T> RadioGroup(
    modifier: Modifier = Modifier,
    onCheckedRequest: (T) -> Boolean,
    content: RadioGroupScope<T>.() -> Unit,
) {
    val scope = remember(content) { RadioGroupScope<T>().apply(content) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        scope.items.forEachIndexed { index, item ->
            RadioGroupButton(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                checked = onCheckedRequest(item.value),
                position = calculatePosition(index, scope.items.size),
            )
        }
    }
}

@Composable
private fun <T> RadioGroupButton(
    modifier: Modifier = Modifier,
    item: RadioGroupItem<T>,
    checked: Boolean,
    enabled: Boolean = true,
    position: RadioGroupItemPosition,
) {
    ToggleButton(
        checked = checked,
        onCheckedChange = { item.onValueChanged(item.value) },
        modifier = modifier
            .semantics { role = Role.RadioButton }
            .fillMaxWidth(),
        shapes = createShapes(position),
        enabled = enabled,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = checked,
                onClick = null,
                enabled = enabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = if (checked) MaterialTheme.colorScheme.onPrimary else Color.Unspecified,
                ),
            )
            Spacer(modifier = Modifier.size(ToggleButtonDefaults.IconSpacing))
            item.content()
        }
    }
}

private fun createShapes(position: RadioGroupItemPosition): ToggleButtonShapes {
    return when (position) {
        RadioGroupItemPosition.SOLO -> ToggleButtonShapes(
            shape = RoundedCornerShape(outerCorner),
            pressedShape = RoundedCornerShape(pressedCorner),
            checkedShape = RoundedCornerShape(checkedCorner),
        )

        RadioGroupItemPosition.LEADING -> ToggleButtonShapes(
            shape = RoundedCornerShape(
                topStart = outerCorner,
                topEnd = outerCorner,
                bottomStart = innerCorner,
                bottomEnd = innerCorner,
            ),
            pressedShape = RoundedCornerShape(
                topStart = pressedCorner,
                topEnd = pressedCorner,
                bottomStart = innerCorner,
                bottomEnd = innerCorner,
            ),
            checkedShape = RoundedCornerShape(checkedCorner),
        )

        RadioGroupItemPosition.MIDDLE -> ToggleButtonShapes(
            shape = RoundedCornerShape(innerCorner),
            pressedShape = RoundedCornerShape(pressedCorner),
            checkedShape = RoundedCornerShape(checkedCorner),
        )

        RadioGroupItemPosition.TRAILING -> ToggleButtonShapes(
            shape = RoundedCornerShape(
                topStart = innerCorner,
                topEnd = innerCorner,
                bottomStart = outerCorner,
                bottomEnd = outerCorner,
            ),
            pressedShape = RoundedCornerShape(
                topStart = innerCorner,
                topEnd = innerCorner,
                bottomStart = pressedCorner,
                bottomEnd = pressedCorner,
            ),
            checkedShape = RoundedCornerShape(checkedCorner),
        )
    }
}

private enum class Mode { AUTO, LIGHT, DARK }

@Preview(showBackground = true)
@Composable
private fun RadioGroupPreview1() {
    var selectedMode by remember { mutableStateOf(Mode.AUTO) }
    SequenceTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            RadioGroup(
                modifier = Modifier.width(200.dp),
                onCheckedRequest = { it == selectedMode }
            ) {
                item(
                    value = Mode.AUTO,
                    onValueChanged = { selectedMode = it },
                    content = { Text("Auto") },
                )
                item(
                    value = Mode.LIGHT,
                    onValueChanged = { selectedMode = it },
                    content = { Text("Light") },
                )
                item(
                    value = Mode.DARK,
                    onValueChanged = { selectedMode = it },
                    content = { Text("Dark") },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioGroupPreview2() {
    var selectedMode by remember { mutableStateOf(Mode.AUTO) }
    SequenceTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            RadioGroup(
                modifier = Modifier.width(200.dp),
                onCheckedRequest = { it == selectedMode },
            ) {
                item(
                    value = Mode.AUTO,
                    onValueChanged = { selectedMode = it },
                    content = { Text("Auto") },
                )
            }
        }
    }
}