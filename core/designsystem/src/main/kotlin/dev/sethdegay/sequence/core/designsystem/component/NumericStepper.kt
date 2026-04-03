package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sethdegay.sequence.core.designsystem.R.string
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.IconButton

@Composable
fun NumericStepper(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    fontSize: TextUnit = 24.sp,
    minValue: Int = 0,
    maxValue: Int = Int.MAX_VALUE,
    contentPadding: Dp = 8.dp,
    spacing: Dp = 12.dp,
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        SequenceIcons.KeyboardArrowUp.IconButton(
            onClick = {
                val value = value + 1
                if (value <= maxValue) {
                    onValueChange(value)
                }
            },
            enabled = value < maxValue,
            contentDescription = stringResource(string.increment_content_description),
        )
        BasicTextField(
            value = value.toString().padStart(2, '0'),
            onValueChange = {
                val newValue = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
                onValueChange(newValue)
            },
            readOnly = true,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = fontSize,
            ),
        )
        SequenceIcons.KeyboardArrowDown.IconButton(
            onClick = {
                val value = value - 1
                if (value >= minValue) {
                    onValueChange(value)
                }
            },
            enabled = value > minValue,
            contentDescription = stringResource(string.decrement_content_description),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NumericStepperPreview() {
    val (value, onValueChange) = remember { mutableIntStateOf(0) }
    NumericStepper(
        value = value,
        onValueChange = onValueChange,
        maxValue = 10,
    )
}