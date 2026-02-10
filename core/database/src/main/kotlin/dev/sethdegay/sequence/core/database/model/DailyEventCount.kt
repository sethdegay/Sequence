package dev.sethdegay.sequence.core.database.model

import kotlinx.datetime.LocalDate

data class DailyEventCount(val date: LocalDate, val count: Int)
