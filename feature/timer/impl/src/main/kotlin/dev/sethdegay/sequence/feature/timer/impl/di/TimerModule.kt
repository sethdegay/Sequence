package dev.sethdegay.sequence.feature.timer.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.timer.api.TimerNavKey
import dev.sethdegay.sequence.feature.timer.impl.TimerScreen
import dev.sethdegay.sequence.feature.timer.impl.TimerViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object TimerModule {
    @IntoSet
    @Provides
    fun providesNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<TimerNavKey> { key ->
            TimerScreen(
                viewModel = hiltViewModel<TimerViewModel, TimerViewModel.Factory>(
                    creationCallback = { factory -> factory.create(key.id) },
                ),
                navigateUp = navigator::navigateUp,
            )
        }
    }
}