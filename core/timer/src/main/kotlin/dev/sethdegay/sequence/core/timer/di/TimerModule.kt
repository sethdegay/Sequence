package dev.sethdegay.sequence.core.timer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ViewModelLifecycle
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.sethdegay.sequence.core.model.Step
import dev.sethdegay.sequence.core.timer.SequentialTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Module
@InstallIn(ViewModelComponent::class)
object TimerModule {
    @Provides
    @ViewModelScoped
    fun provideSequentialTimer(lifecycle: ViewModelLifecycle): SequentialTimer<Step> {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        lifecycle.addOnClearedListener { scope.cancel() }
        return SequentialTimer(
            scope = scope,
            durationProvider = { it.duration },
        )
    }
}