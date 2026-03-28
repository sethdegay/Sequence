package dev.sethdegay.sequence.feature.calendarevent.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EventListNav(val range: ClosedRange<Instant>) : NavKey