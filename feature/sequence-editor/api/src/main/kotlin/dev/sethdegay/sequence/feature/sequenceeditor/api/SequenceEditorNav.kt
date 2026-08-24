package dev.sethdegay.sequence.feature.sequenceeditor.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed class SequenceEditorNav : NavKey {
    @Serializable
    data class Create(val libraryId: Uuid) : SequenceEditorNav()

    @Serializable
    data class Edit(val sequenceId: Uuid, val libraryId: Uuid) : SequenceEditorNav()
}