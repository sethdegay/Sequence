package dev.sethdegay.sequence.feature.editor.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.editor.api.EditorNavKey
import dev.sethdegay.sequence.feature.editor.impl.EditorScreen
import dev.sethdegay.sequence.feature.editor.impl.EditorViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object EditorModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<EditorNavKey> { key ->
            EditorScreen(
                viewModel = hiltViewModel<EditorViewModel, EditorViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key.sequenceId, key.workspaceId)
                    },
                ),
                navigateUp = navigator::navigateUp,
            )
        }
    }
}