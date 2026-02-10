package dev.sethdegay.sequence.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

internal class LocalDateConverter {
    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate = LocalDate.parse(value)
}