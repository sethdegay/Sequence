package dev.sethdegay.sequence.core.navigation.di

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.sethdegay.sequence.core.navigation.HomeRoute

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {
    @Provides
    @ActivityRetainedScoped
    fun provideSequenceBackStackManager(): SequenceBackStackManager =
        SequenceBackStackManager(startDestination = HomeRoute)
}

@ActivityRetainedScoped
class SequenceBackStackManager(startDestination: NavKey) {
    val backStack = mutableStateListOf(startDestination)

    fun navigate(destination: NavKey) {
        backStack.add(destination)
    }

    fun navigateUp() {
        backStack.removeLastOrNull()
    }
}