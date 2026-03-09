package dev.sethdegay.sequence.core.database.util

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val DURATION_UNIT = DurationUnit.SECONDS

internal class DurationLongConverter {
    @TypeConverter
    fun longToDuration(value: Long?): Duration? = value?.toDuration(DURATION_UNIT)

    @TypeConverter
    fun durationToLong(value: Duration?): Long? = value?.toLong(DURATION_UNIT)
}
