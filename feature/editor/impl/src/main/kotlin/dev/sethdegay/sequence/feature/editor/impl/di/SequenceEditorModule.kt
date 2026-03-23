package dev.sethdegay.sequence.feature.editor.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.editor.api.SegmentEditorNav
import dev.sethdegay.sequence.feature.editor.api.SequenceEditorNav
import dev.sethdegay.sequence.feature.editor.impl.SegmentEditorContainer
import dev.sethdegay.sequence.feature.editor.impl.SegmentEditorViewModel
import dev.sethdegay.sequence.feature.editor.impl.SequenceEditorScreen
import dev.sethdegay.sequence.feature.editor.impl.SequenceEditorViewModel

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
                        factory.create(key.sequenceId, key.workspaceId)
                    },
                ),
                navigateToSegmentEditor = navigator::navigate,
                navigateUp = navigator::navigateUp,
            )
        }
        entry<SegmentEditorNav.Create>(metadata = BottomSheetSceneStrategy.bottomSheetMetadata()) { key ->
            SegmentEditorContainer(
                viewModel = hiltViewModel<SegmentEditorViewModel, SegmentEditorViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(
                            segmentId = null,
                            sequenceId = key.sequenceId,
                            lastSegmentPosition = key.lastSegmentPosition,
                        )
                    },
                ),
                navigateUp = navigator::navigateUp,
            )
        }
        entry<SegmentEditorNav.Edit>(metadata = BottomSheetSceneStrategy.bottomSheetMetadata()) { key ->
            SegmentEditorContainer(
                viewModel = hiltViewModel<SegmentEditorViewModel, SegmentEditorViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(
                            segmentId = key.segmentId,
                            sequenceId = key.sequenceId,
                            lastSegmentPosition = null,
                        )
                    },
                ),
                navigateUp = navigator::navigateUp,
            )
        }
    }
}