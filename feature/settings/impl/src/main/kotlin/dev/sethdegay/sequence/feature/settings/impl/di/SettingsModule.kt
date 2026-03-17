package dev.sethdegay.sequence.feature.settings.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.settings.api.SettingsNavKey
import dev.sethdegay.sequence.feature.settings.impl.SettingsScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
object SettingsModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<SettingsNavKey> {
            SettingsScreen(
                viewModel = hiltViewModel(),
                navigateUp = navigator::navigateUp,
            )
        }
    }
}