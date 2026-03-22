package dev.sethdegay.sequence.feature.editor.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed class SegmentNav : NavKey {
    @Serializable
    data class Create(
        val sequenceId: Uuid,
        val lastSegmentPosition: Int,
    ) : SegmentNav()

    @Serializable
    data class Edit(
        val segmentId: Uuid,
        val sequenceId: Uuid,
    ) : SegmentNav()
}