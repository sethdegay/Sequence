package dev.sethdegay.routines.feature.settings.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.routines.feature.settings.SettingsScreen
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SettingsRoute
import dev.sethdegay.sequence.core.navigation.di.SequenceBackStackManager

@Module
@InstallIn(ActivityRetainedComponent::class)
object SettingsModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(backStackManager: SequenceBackStackManager): NavKeyInstaller = {
        entry<SettingsRoute> {
            SettingsScreen(
                viewModel = hiltViewModel(),
                navigateUp = backStackManager::navigateUp,
            )
        }
    }
}