package dev.sethdegay.routines.feature.settings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.routines.core.di.RoutinesBackStackManager
import dev.sethdegay.routines.core.navigation.NavKeyInstaller
import dev.sethdegay.routines.core.navigation.SettingsRoute
import dev.sethdegay.routines.feature.settings.SettingsScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
object SettingsModule {

    @IntoSet
    @Provides
    fun provideNavKeyInstaller(backStackManager: RoutinesBackStackManager): NavKeyInstaller = {
        entry<SettingsRoute> {
            SettingsScreen(
                navigateUp = backStackManager::navigateUp,
            )
        }
    }
}