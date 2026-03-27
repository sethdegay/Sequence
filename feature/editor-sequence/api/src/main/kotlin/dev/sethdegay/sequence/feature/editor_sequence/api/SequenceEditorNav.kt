package dev.sethdegay.sequence.feature.editor_sequence.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class SequenceEditorNav(val sequenceId: Uuid?, val libraryId: Uuid) : NavKey