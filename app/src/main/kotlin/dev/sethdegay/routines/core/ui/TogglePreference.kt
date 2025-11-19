package dev.sethdegay.routines.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.core.designsystem.component.ToggleButtonOption
import dev.sethdegay.routines.core.designsystem.component.ToggleButtons
import dev.sethdegay.routines.core.designsystem.component.VerticalListEntry

@Composable
fun <T> TogglePreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    spaceBetween: Dp = 16.dp,
    options: List<ToggleButtonOption<T>>,
    onCheckedRequest: (T) -> Boolean,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spaceBetween),
    ) {
        VerticalListEntry(
            title = title,
            description = description,
        )
        ToggleButtons(
            options = options,
            onCheckedRequest = onCheckedRequest,
        )
    }
}