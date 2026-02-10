package dev.sethdegay.sequence.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sethdegay.sequence.core.database.dao.CalendarEventDao
import dev.sethdegay.sequence.core.database.dao.SequenceDao
import dev.sethdegay.sequence.core.database.model.CalendarEventEntity
import dev.sethdegay.sequence.core.database.model.SequenceEntity
import dev.sethdegay.sequence.core.database.model.StepEntity
import dev.sethdegay.sequence.core.database.util.DurationConverter
import dev.sethdegay.sequence.core.database.util.InstantConverter
import dev.sethdegay.sequence.core.database.util.LocalDateConverter

@Database(
    entities = [
        CalendarEventEntity::class,
        SequenceEntity::class,
        StepEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        DurationConverter::class,
        InstantConverter::class,
        LocalDateConverter::class,
    ],
)
abstract class SequenceDatabase : RoomDatabase() {
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun sequenceDao(): SequenceDao
}