package dev.sethdegay.sequence.feature.editor_segment.impl.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.editor_segment.api.SegmentEditorNav
import dev.sethdegay.sequence.feature.editor_segment.impl.SegmentEditorContainer
import dev.sethdegay.sequence.feature.editor_segment.impl.SegmentEditorViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object SegmentEditorModule {
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
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