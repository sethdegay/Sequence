package dev.sethdegay.sequence.feature.segment.editor.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed class SegmentEditorNav : NavKey {
    @Serializable
    data class Create(
        val sequenceId: Uuid,
        val lastSegmentPosition: Int,
    ) : SegmentEditorNav()

    @Serializable
    data class Edit(
        val segmentId: Uuid,
        val sequenceId: Uuid,
    ) : SegmentEditorNav()
}