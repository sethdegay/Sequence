package dev.sethdegay.sequence.feature.sequencecontextmenu.impl.di

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.sequencecontextmenu.api.SequenceContextMenuNav
import dev.sethdegay.sequence.feature.sequencecontextmenu.impl.SequenceContextMenu
import dev.sethdegay.sequence.feature.sequencecontextmenu.impl.SequenceContextMenuViewModel
import dev.sethdegay.sequence.feature.sequenceeditor.api.SequenceEditorNav
import kotlin.uuid.ExperimentalUuidApi

@Module
@InstallIn(ActivityRetainedComponent::class)
object SequenceContextMenuModule {
    @OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
    @IntoSet
    @Provides
    fun provideNavKeyInstaller(navigator: SequenceNavigator): NavKeyInstaller = {
        entry<SequenceContextMenuNav>(metadata = BottomSheetSceneStrategy.bottomSheetMetadata()) {
            SequenceContextMenu(
                viewModel = hiltViewModel<SequenceContextMenuViewModel, SequenceContextMenuViewModel.Factory>(
                    creationCallback = { factory -> factory.create(it.sequenceId, it.libraryId) }
                ),
                navigateUp = navigator::navigateUp,
                editSequence = { sequenceId, libraryId ->
                    navigator.replaceLast(SequenceEditorNav.Edit(sequenceId, libraryId))
                },
            )
        }
    }
}