package dev.sethdegay.sequence.feature.sequencecontextmenu.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class SequenceContextMenuNav(val sequenceId: Uuid, val libraryId: Uuid) : NavKey