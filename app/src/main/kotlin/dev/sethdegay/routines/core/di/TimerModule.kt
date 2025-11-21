package dev.sethdegay.routines.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ViewModelLifecycle
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.timer.SequentialTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Module
@InstallIn(ViewModelComponent::class)
object TimerModule {
    @Provides
    @ViewModelScoped
    fun provideSequentialTimer(lifecycle: ViewModelLifecycle): SequentialTimer<Task> {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        lifecycle.addOnClearedListener { scope.cancel() }
        return SequentialTimer(
            scope = scope,
            durationProvider = { it.duration },
        )
    }
}