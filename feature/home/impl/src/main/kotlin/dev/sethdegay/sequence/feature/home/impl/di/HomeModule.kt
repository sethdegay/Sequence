package dev.sethdegay.sequence.feature.home.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.editor_sequence.api.SequenceEditorNav
import dev.sethdegay.sequence.feature.home.api.EventListNav
import dev.sethdegay.sequence.feature.home.api.HomeNav
import dev.sethdegay.sequence.feature.home.impl.EventListContainer
import dev.sethdegay.sequence.feature.home.impl.EventListViewModel
import dev.sethdegay.sequence.feature.home.impl.HomeScreen
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
                navigateToEventList = { navigator.navigate(EventListNav(it)) },
                navigateToSequenceEditor = { id, workspaceId ->
                    navigator.navigate(SequenceEditorNav(id, workspaceId))
                },
                navigateToSettings = { navigator.navigate(SettingsNav) },
                navigateToTimer = { id -> navigator.navigate(TimerNav(id)) },
            )
        }
        entry<EventListNav>(metadata = BottomSheetSceneStrategy.bottomSheetMetadata()) {
            EventListContainer(
                viewModel = hiltViewModel<EventListViewModel, EventListViewModel.Factory>(
                    creationCallback = { factory -> factory.create(it.range) },
                ),
            )
        }
    }
}