package dev.sethdegay.routines.core.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.ToggleButtonOption
import dev.sethdegay.routines.core.designsystem.component.ToggleButtonPosition
import dev.sethdegay.routines.core.designsystem.component.ToggleButtons
import dev.sethdegay.routines.core.designsystem.component.VerticalListEntry
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.model.ThemeConfig

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

@Composable
fun ThemePreference(
    context: Context = LocalContext.current,
    contentPadding: PaddingValues = PaddingValues(),
    onCheckedRequest: (ThemeConfig) -> Boolean,
    onThemeConfigChanged: (ThemeConfig) -> Unit,
) {
    TogglePreference(
        modifier = Modifier.padding(contentPadding),
        title = stringResource(string.settings_theme_title),
        description = stringResource(string.settings_theme_description),
        options = remember {
            listOf(
                ToggleButtonOption(
                    label = context.getString(string.settings_system_theme_title),
                    iconChecked = RoutinesIcons.SystemChecked,
                    iconUnchecked = RoutinesIcons.SystemUnchecked,
                    value = ThemeConfig.FOLLOW_SYSTEM,
                    onValueChanged = onThemeConfigChanged,
                    toggleButtonPosition = ToggleButtonPosition.LEADING,
                ),
                ToggleButtonOption(
                    label = context.getString(string.settings_light_theme_title),
                    iconChecked = RoutinesIcons.LightModeChecked,
                    iconUnchecked = RoutinesIcons.LightModeUnchecked,
                    value = ThemeConfig.LIGHT,
                    onValueChanged = onThemeConfigChanged,
                    toggleButtonPosition = ToggleButtonPosition.MIDDLE,
                ),
                ToggleButtonOption(
                    label = context.getString(string.settings_dark_theme_title),
                    iconChecked = RoutinesIcons.DarkModeChecked,
                    iconUnchecked = RoutinesIcons.DarkModeUnchecked,
                    value = ThemeConfig.DARK,
                    onValueChanged = onThemeConfigChanged,
                    toggleButtonPosition = ToggleButtonPosition.TRAILING,
                ),
            )
        },
        onCheckedRequest = onCheckedRequest,
    )
}
