package dev.sethdegay.routines.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import dev.sethdegay.routines.R.drawable

object RoutinesIcons {
    val DarkModeChecked @Composable get() = ImageVector.vectorResource(drawable.dark_mode_checked_24px)
    val DarkModeUnchecked @Composable get() = ImageVector.vectorResource(drawable.dark_mode_unchecked_24px)
    val DragHandle @Composable get() = ImageVector.vectorResource(drawable.drag_handle_24px)
    val LightModeChecked @Composable get() = ImageVector.vectorResource(drawable.light_mode_checked_24px)
    val LightModeUnchecked @Composable get() = ImageVector.vectorResource(drawable.light_mode_unchecked_24px)
    val NavigateUp @Composable get() = ImageVector.vectorResource(drawable.arrow_back_24px)
    val Next @Composable get() = ImageVector.vectorResource(drawable.skip_next_24px)
    val Previous @Composable get() = ImageVector.vectorResource(drawable.skip_previous_24px)
    val SystemChecked @Composable get() = ImageVector.vectorResource(drawable.mobile_3_checked_24px)
    val SystemUnchecked @Composable get() = ImageVector.vectorResource(drawable.mobile_3_unchecked_24px)
}