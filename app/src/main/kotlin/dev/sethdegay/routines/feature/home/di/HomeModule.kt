package dev.sethdegay.routines.feature.home.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.routines.feature.home.HomeScreen
import dev.sethdegay.sequence.core.navigation.EditorRoute
import dev.sethdegay.sequence.core.navigation.HomeRoute
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SettingsRoute
import dev.sethdegay.sequence.core.navigation.TimerRoute
import dev.sethdegay.sequence.core.navigation.di.SequenceBackStackManager

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(backStackManager: SequenceBackStackManager): NavKeyInstaller = {
        entry<HomeRoute> {
            HomeScreen(
                viewModel = hiltViewModel(),
                navigateToEditor = { id -> backStackManager.navigate(EditorRoute(id)) },
                navigateToSettings = { backStackManager.navigate(SettingsRoute) },
                navigateToTimer = { id -> backStackManager.navigate(TimerRoute(id)) },
            )
        }
    }
}