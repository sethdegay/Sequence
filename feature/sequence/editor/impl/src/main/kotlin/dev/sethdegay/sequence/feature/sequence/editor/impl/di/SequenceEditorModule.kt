package dev.sethdegay.sequence.feature.sequence.editor.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.sequence.editor.api.SequenceEditorNav
import dev.sethdegay.sequence.feature.sequence.editor.impl.SequenceEditorScreen
import dev.sethdegay.sequence.feature.sequence.editor.impl.SequenceEditorViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object SequenceEditorModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<SequenceEditorNav> { key ->
            SequenceEditorScreen(
                viewModel = hiltViewModel<SequenceEditorViewModel, SequenceEditorViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key.sequenceId, key.libraryId)
                    },
                ),
                navigateToSegmentEditor = navigator::navigate,
                navigateUp = navigator::navigateUp,
            )
        }
    }
}