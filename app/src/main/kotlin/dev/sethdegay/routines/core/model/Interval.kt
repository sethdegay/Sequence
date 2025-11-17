package dev.sethdegay.routines.core.model

import kotlin.time.Duration

data class Interval(
    val id: Long? = null,
    val title: String,
    val duration: Duration,
    val order: Int = 0,
)