package dev.sethdegay.sequence.feature.calendarlogs.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.feature.calendarlogs.api.CalendarLogsNav
import dev.sethdegay.sequence.feature.calendarlogs.impl.CalendarLogsContainer
import dev.sethdegay.sequence.feature.calendarlogs.impl.CalendarLogsViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object CalendarLogsModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(): NavKeyInstaller = {
        entry<CalendarLogsNav>(metadata = BottomSheetSceneStrategy.bottomSheetMetadata()) {
            CalendarLogsContainer(
                viewModel = hiltViewModel<CalendarLogsViewModel, CalendarLogsViewModel.Factory>(
                    creationCallback = { factory -> factory.create(it.range) },
                ),
            )
        }
    }
}