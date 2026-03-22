package dev.sethdegay.sequence.feature.editor.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class EditorNavKey(val sequenceId: Uuid?, val workspaceId: Uuid) : NavKey