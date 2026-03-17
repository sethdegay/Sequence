package dev.sethdegay.sequence.feature.timer.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class TimerNavKey(val id: Uuid) : NavKey