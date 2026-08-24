package dev.sethdegay.sequence.feature.home.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.calendarlogs.api.CalendarLogsNav
import dev.sethdegay.sequence.feature.home.api.HomeNav
import dev.sethdegay.sequence.feature.home.impl.HomeScreen
import dev.sethdegay.sequence.feature.sequencecontextmenu.api.SequenceContextMenuNav
import dev.sethdegay.sequence.feature.sequenceeditor.api.SequenceEditorNav
import dev.sethdegay.sequence.feature.settings.api.SettingsNav
import dev.sethdegay.sequence.feature.timer.api.TimerNav

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<HomeNav> {
            HomeScreen(
                viewModel = hiltViewModel(),
                navigateToEventList = { navigator.navigate(CalendarLogsNav(it)) },
                createSequence = { libraryId ->
                    navigator.navigate(SequenceEditorNav.Create(libraryId))
                },
                navigateToSequenceContextMenu = { sequenceId, libraryId ->
                    navigator.navigate(SequenceContextMenuNav(sequenceId, libraryId))
                },
                navigateToSettings = { navigator.navigate(SettingsNav) },
                navigateToTimer = { id -> navigator.navigate(TimerNav(id)) },
            )
        }
    }
}