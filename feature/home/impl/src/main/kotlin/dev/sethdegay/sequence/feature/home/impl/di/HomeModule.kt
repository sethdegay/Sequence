package dev.sethdegay.sequence.feature.home.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.editor.api.EditorNavKey
import dev.sethdegay.sequence.feature.home.api.HomeNavKey
import dev.sethdegay.sequence.feature.home.impl.HomeScreen
import dev.sethdegay.sequence.feature.settings.api.SettingsNavKey
import dev.sethdegay.sequence.feature.timer.api.TimerNavKey

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<HomeNavKey> {
            HomeScreen(
                viewModel = hiltViewModel(),
                navigateToEditor = { id, workspaceId ->
                    navigator.navigate(EditorNavKey(id, workspaceId))
                },
                navigateToSettings = { navigator.navigate(SettingsNavKey) },
                navigateToTimer = { id -> navigator.navigate(TimerNavKey(id)) },
            )
        }
    }
}