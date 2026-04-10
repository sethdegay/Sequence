package dev.sethdegay.sequence.feature.license.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.license.api.LicenseNav
import dev.sethdegay.sequence.feature.license.impl.LicenseScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
object LicenseModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<LicenseNav> { LicenseScreen(navigateUp = navigator::navigateUp) }
    }
}