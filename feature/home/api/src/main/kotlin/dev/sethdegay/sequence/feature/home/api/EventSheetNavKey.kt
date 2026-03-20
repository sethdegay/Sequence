package dev.sethdegay.sequence.feature.home.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EventSheetNavKey(val range: ClosedRange<Instant>) : NavKey