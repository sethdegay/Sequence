package dev.sethdegay.sequence.navigation

import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy

@Module
@InstallIn(ActivityRetainedComponent::class)
object SequenceSceneStrategyModule {
    @Provides
    @ActivityRetainedScoped
    fun provideBottomSheetSceneStrategy(): BottomSheetSceneStrategy<NavKey> =
        BottomSheetSceneStrategy()
}