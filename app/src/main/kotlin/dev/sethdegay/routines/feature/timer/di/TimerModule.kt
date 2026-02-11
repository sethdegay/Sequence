package dev.sethdegay.routines.feature.timer.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.routines.feature.timer.TimerScreen
import dev.sethdegay.routines.feature.timer.TimerViewModel
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.TimerRoute
import dev.sethdegay.sequence.core.navigation.di.SequenceBackStackManager

@Module
@InstallIn(ActivityRetainedComponent::class)
object TimerModule {
    @IntoSet
    @Provides
    fun providesNavKeyInstaller(backStackManager: SequenceBackStackManager): NavKeyInstaller = {
        entry<TimerRoute> { key ->
            TimerScreen(
                viewModel = hiltViewModel<TimerViewModel, TimerViewModel.Factory>(
                    creationCallback = { factory -> factory.create(key.id) }
                ),
                navigateUp = backStackManager::navigateUp,
            )
        }
    }
}