package dev.sethdegay.routines.core.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.ToggleButtonOption
import dev.sethdegay.routines.core.designsystem.component.ToggleButtonPosition
import dev.sethdegay.routines.core.designsystem.component.ToggleButtons
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.model.ThemeConfig

@Composable
fun <T> TogglePreference(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    options: @Composable () -> List<ToggleButtonOption<T>>,
    onCheckedRequest: (T) -> Boolean,
) {
    Column(modifier = modifier) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = description?.let { { Text(it) } },
            colors = ListItemDefaults.colors(
                containerColor = CardDefaults.cardColors().containerColor,
                headlineColor = CardDefaults.cardColors().contentColor,
                supportingColor = CardDefaults.cardColors().contentColor,
            ),
        )
        ToggleButtons(
            modifier = Modifier.padding(
                // See ItemXSpace in: androidx.compose.material3.tokens.ListTokens
                top = 0.dp,
                start = 12.dp,
                bottom = 12.dp,
                end = 12.dp
            ),
            options = options.invoke(),
            onCheckedRequest = onCheckedRequest,
        )
    }
}

@Composable
fun ThemePreference(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onCheckedRequest: (ThemeConfig) -> Boolean,
    onThemeConfigChanged: (ThemeConfig) -> Unit,
) {
    TogglePreference(
        modifier = modifier,
        title = stringResource(string.settings_theme_title),
        description = stringResource(string.settings_theme_description),
        options = {
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
