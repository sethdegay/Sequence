package dev.sethdegay.sequence.feature.calendarevent.list.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.feature.calendarevent.list.api.EventListNav
import dev.sethdegay.sequence.feature.calendarevent.list.impl.EventListContainer
import dev.sethdegay.sequence.feature.calendarevent.list.impl.EventListViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object CalendarEventModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(): NavKeyInstaller = {
        entry<EventListNav>(metadata = BottomSheetSceneStrategy.bottomSheetMetadata()) {
            EventListContainer(
                viewModel = hiltViewModel<EventListViewModel, EventListViewModel.Factory>(
                    creationCallback = { factory -> factory.create(it.range) },
                ),
            )
        }
    }
}