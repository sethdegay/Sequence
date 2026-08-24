package dev.sethdegay.sequence.feature.calendarlogs.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class CalendarLogsNav(val range: ClosedRange<Instant>) : NavKey