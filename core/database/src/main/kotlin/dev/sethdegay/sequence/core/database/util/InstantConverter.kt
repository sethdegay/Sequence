package dev.sethdegay.sequence.core.database.util

import androidx.room.TypeConverter
import kotlin.time.Instant

internal class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long): Instant = Instant.fromEpochSeconds(value)

    @TypeConverter
    fun instantToLong(value: Instant): Long = value.epochSeconds
}