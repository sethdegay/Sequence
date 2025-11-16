package dev.sethdegay.routines.core.di

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.sethdegay.routines.core.navigation.HomeRoute

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideRoutinesBackStackManager(): RoutinesBackStackManager =
        RoutinesBackStackManager(startDestination = HomeRoute)
}

@ActivityRetainedScoped
class RoutinesBackStackManager(startDestination: NavKey) {
    val backStack = mutableStateListOf(startDestination)

    fun navigate(destination: NavKey) {
        backStack.add(destination)
    }

    fun navigateUp() {
        backStack.removeLastOrNull()
    }
}