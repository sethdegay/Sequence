package dev.sethdegay.routines.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sethdegay.routines.core.database.dao.CalendarEventDao
import dev.sethdegay.routines.core.database.dao.RoutineDao
import dev.sethdegay.routines.core.database.model.CalendarEventEntity
import dev.sethdegay.routines.core.database.model.RoutineEntity
import dev.sethdegay.routines.core.database.model.TaskEntity
import dev.sethdegay.routines.core.database.util.DurationConverter
import dev.sethdegay.routines.core.database.util.InstantConverter
import dev.sethdegay.routines.core.database.util.LocalDateConverter

@Database(
    entities = [
        CalendarEventEntity::class,
        TaskEntity::class,
        RoutineEntity::class,
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
abstract class RoutinesDatabase : RoomDatabase() {
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun routineDao(): RoutineDao
}