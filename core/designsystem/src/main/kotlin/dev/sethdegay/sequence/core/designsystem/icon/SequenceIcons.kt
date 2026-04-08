package dev.sethdegay.sequence.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import dev.sethdegay.sequence.core.designsystem.R.drawable

object SequenceIcons {
    val Add @Composable get() = ImageVector.vectorResource(drawable.add_24px)
    val DarkModeChecked @Composable get() = ImageVector.vectorResource(drawable.dark_mode_checked_24px)
    val DarkModeUnchecked @Composable get() = ImageVector.vectorResource(drawable.dark_mode_unchecked_24px)
    val DragHandle @Composable get() = ImageVector.vectorResource(drawable.drag_handle_24px)
    val KeyboardChecked @Composable get() = ImageVector.vectorResource(drawable.keyboard_checked_24px)
    val KeyboardUnchecked @Composable get() = ImageVector.vectorResource(drawable.keyboard_unchecked_24px)
    val KeyboardArrowUp @Composable get() = ImageVector.vectorResource(drawable.keyboard_arrow_up_24px)
    val KeyboardArrowDown @Composable get() = ImageVector.vectorResource(drawable.keyboard_arrow_down_24px)
    val LightModeChecked @Composable get() = ImageVector.vectorResource(drawable.light_mode_checked_24px)
    val LightModeUnchecked @Composable get() = ImageVector.vectorResource(drawable.light_mode_unchecked_24px)
    val NavigateUp @Composable get() = ImageVector.vectorResource(drawable.arrow_back_24px)
    val Next @Composable get() = ImageVector.vectorResource(drawable.skip_next_24px)
    val PickChecked @Composable get() = ImageVector.vectorResource(drawable.pick_checked_24px)
    val PickUnchecked @Composable get() = ImageVector.vectorResource(drawable.pick_unchecked_24px)
    val PlayArrow @Composable get() = ImageVector.vectorResource(drawable.play_arrow_24px)
    val Previous @Composable get() = ImageVector.vectorResource(drawable.skip_previous_24px)
    val Settings @Composable get() = ImageVector.vectorResource(drawable.settings_24px)
    val SystemChecked @Composable get() = ImageVector.vectorResource(drawable.routine_checked_24px)
    val SystemUnchecked @Composable get() = ImageVector.vectorResource(drawable.routine_unchecked_24px)
}