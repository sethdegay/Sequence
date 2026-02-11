package dev.sethdegay.routines.feature.editor.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.routines.feature.editor.EditorScreen
import dev.sethdegay.routines.feature.editor.EditorViewModel
import dev.sethdegay.sequence.core.navigation.EditorRoute
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.di.SequenceBackStackManager

@Module
@InstallIn(ActivityRetainedComponent::class)
object EditorModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(backStackManager: SequenceBackStackManager): NavKeyInstaller = {
        entry<EditorRoute> { key ->
            EditorScreen(
                viewModel = hiltViewModel<EditorViewModel, EditorViewModel.Factory>(
                    creationCallback = { factory -> factory.create(key.id) }
                ),
                navigateUp = backStackManager::navigateUp,
            )
        }
    }
}