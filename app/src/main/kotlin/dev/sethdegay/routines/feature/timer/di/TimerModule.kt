package dev.sethdegay.routines.feature.timer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.routines.core.di.RoutinesBackStackManager
import dev.sethdegay.routines.core.navigation.NavKeyInstaller
import dev.sethdegay.routines.core.navigation.TimerRoute
import dev.sethdegay.routines.feature.timer.TimerScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
object TimerModule {

    @IntoSet
    @Provides
    fun providesNavKeyInstaller(backStackManager: RoutinesBackStackManager): NavKeyInstaller = {
        entry<TimerRoute> { key ->
            TimerScreen(
                id = key.id,
                navigateUp = backStackManager::navigateUp,
            )
        }
    }
}