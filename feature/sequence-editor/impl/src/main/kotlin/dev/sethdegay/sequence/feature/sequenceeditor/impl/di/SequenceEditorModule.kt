package dev.sethdegay.sequence.feature.sequenceeditor.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.sequenceeditor.api.SequenceEditorNav
import dev.sethdegay.sequence.feature.sequenceeditor.impl.SequenceEditorScreen
import dev.sethdegay.sequence.feature.sequenceeditor.impl.SequenceEditorViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object SequenceEditorModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<SequenceEditorNav.Create> { key ->
            SequenceEditorScreen(
                viewModel = hiltViewModel<SequenceEditorViewModel, SequenceEditorViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(null, key.libraryId)
                    },
                ),
                navigateToSegmentEditor = navigator::navigate,
                navigateUp = navigator::navigateUp,
            )
        }
        entry<SequenceEditorNav.Edit> { key ->
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