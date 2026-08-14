package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.theme.SequenceTheme
import dev.sethdegay.sequence.core.designsystem.util.Icon

@Composable
fun DropdownButton(
    text: String,
    onClick: () -> Unit,
    style: TextStyle = LocalTextStyle.current,
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = style,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingIcon = {
            SequenceIcons.KeyboardArrowDown.Icon(
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = AssistChipDefaults.assistChipColors(),
        border = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun DropdownButtonPreview() {
    SequenceTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            DropdownButton(
                text = "Hello",
                onClick = {},
            )
        }
    }
}