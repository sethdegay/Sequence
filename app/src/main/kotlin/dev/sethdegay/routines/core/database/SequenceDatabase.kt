package dev.sethdegay.routines.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sethdegay.routines.core.database.dao.CalendarEventDao
import dev.sethdegay.routines.core.database.dao.SequenceDao
import dev.sethdegay.routines.core.database.model.CalendarEventEntity
import dev.sethdegay.routines.core.database.model.SequenceEntity
import dev.sethdegay.routines.core.database.model.StepEntity
import dev.sethdegay.routines.core.database.util.DurationConverter
import dev.sethdegay.routines.core.database.util.InstantConverter
import dev.sethdegay.routines.core.database.util.LocalDateConverter

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